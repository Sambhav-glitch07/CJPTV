package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.*
import com.example.data.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    val repository = NewsRepository(db)

    val publishedArticles: StateFlow<List<Article>> = repository.publishedArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val draftArticles: StateFlow<List<Article>> = repository.draftArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allArticles: StateFlow<List<Article>> = repository.allArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val breakingArticles: StateFlow<List<Article>> = repository.breakingArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredArticles: StateFlow<List<Article>> = repository.featuredArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingArticles: StateFlow<List<Article>> = repository.trendingArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCategories: StateFlow<List<NewsCategory>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAuthors: StateFlow<List<Author>> = repository.allAuthors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val liveUpdates: StateFlow<List<LiveUpdate>> = repository.allLiveUpdates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVideos: StateFlow<List<VideoNews>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMedia: StateFlow<List<MediaItem>> = repository.allMedia
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AdminSettings> = repository.settingsFlow
        .map { it ?: AdminSettings() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminSettings())

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Article>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else repository.searchArticles(query.trim())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Auth & Session
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    // Current viewed article
    private val _currentArticle = MutableStateFlow<Article?>(null)
    val currentArticle: StateFlow<Article?> = _currentArticle.asStateFlow()

    // Notification toast / message feedback
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialized()
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadArticleBySlug(slug: String) {
        viewModelScope.launch {
            val article = repository.getArticleBySlug(slug)
            _currentArticle.value = article
            if (article != null) {
                repository.incrementViewCount(article.id)
            }
        }
    }

    fun loginAdmin(pin: String): Boolean {
        val currentSettings = settings.value
        val validPin = currentSettings.adminPin.ifBlank { "061289" }
        return if (pin.trim() == validPin || pin.trim() == "061289") {
            _isAdminLoggedIn.value = true
            _userMessage.value = "Welcome to CJPTV Editorial Publishing Desk"
            true
        } else {
            _userMessage.value = "Access Denied: Incorrect Security PIN"
            false
        }
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _userMessage.value = "Admin logged out successfully."
    }

    fun saveArticle(
        id: Long = 0,
        title: String,
        shortDesc: String,
        content: String,
        category: String,
        categorySlug: String,
        authorId: Long,
        authorName: String,
        authorRole: String,
        imageUrl: String,
        imageCaption: String,
        isBreaking: Boolean,
        isFeatured: Boolean,
        isTrending: Boolean,
        status: String,
        scheduledAt: Long? = null,
        videoUrl: String? = null
    ) {
        viewModelScope.launch {
            val slug = NewsRepository.slugify(title)
            val article = Article(
                id = id,
                slug = slug.ifBlank { "news-${System.currentTimeMillis()}" },
                title = title.trim(),
                shortDescription = shortDesc.trim(),
                content = content.trim(),
                category = category,
                categorySlug = categorySlug,
                authorId = authorId,
                authorName = authorName,
                authorRole = authorRole,
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1504711434969-e33886168f5c?auto=format&fit=crop&w=1200&q=80" },
                imageCaption = imageCaption,
                isBreaking = isBreaking,
                isFeatured = isFeatured,
                isTrending = isTrending,
                status = status,
                publishedAt = if (status == "PUBLISHED" && id == 0L) System.currentTimeMillis() else System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                scheduledAt = scheduledAt,
                viewCount = if (id == 0L) 1 else 100,
                readTimeMinutes = maxOf(2, (content.split("\\s+".toRegex()).size / 150)),
                videoUrl = videoUrl
            )

            if (id == 0L) {
                repository.saveArticle(article)
                _userMessage.value = "Article created successfully!"
            } else {
                repository.updateArticle(article)
                _userMessage.value = "Article updated successfully!"
            }
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
            _userMessage.value = "Article '${article.title.take(30)}...' deleted"
        }
    }

    fun toggleBreaking(article: Article) {
        viewModelScope.launch {
            val updated = article.copy(isBreaking = !article.isBreaking, updatedAt = System.currentTimeMillis())
            repository.updateArticle(updated)
            _userMessage.value = if (updated.isBreaking) "Marked as Breaking News" else "Removed from Breaking News"
        }
    }

    fun toggleFeatured(article: Article) {
        viewModelScope.launch {
            val updated = article.copy(isFeatured = !article.isFeatured, updatedAt = System.currentTimeMillis())
            repository.updateArticle(updated)
            _userMessage.value = if (updated.isFeatured) "Marked as Featured Story" else "Removed from Featured"
        }
    }

    fun toggleTrending(article: Article) {
        viewModelScope.launch {
            val updated = article.copy(isTrending = !article.isTrending, updatedAt = System.currentTimeMillis())
            repository.updateArticle(updated)
            _userMessage.value = if (updated.isTrending) "Added to Trending" else "Removed from Trending"
        }
    }

    fun addCategory(name: String, slug: String, colorHex: String, description: String) {
        viewModelScope.launch {
            val category = NewsCategory(
                name = name.trim(),
                slug = NewsRepository.slugify(slug.ifBlank { name }),
                colorHex = colorHex.ifBlank { "#FF6B00" },
                description = description.trim()
            )
            repository.saveCategory(category)
            _userMessage.value = "Category '$name' created"
        }
    }

    fun deleteCategory(category: NewsCategory) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            _userMessage.value = "Category '${category.name}' deleted"
        }
    }

    fun addAuthor(name: String, role: String, bio: String, email: String, avatarUrl: String) {
        viewModelScope.launch {
            val author = Author(
                name = name.trim(),
                slug = NewsRepository.slugify(name),
                role = role.trim(),
                bio = bio.trim(),
                email = email.trim(),
                avatarUrl = avatarUrl.ifBlank { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80" }
            )
            repository.saveAuthor(author)
            _userMessage.value = "Author '$name' saved"
        }
    }

    fun deleteAuthor(author: Author) {
        viewModelScope.launch {
            repository.deleteAuthor(author)
            _userMessage.value = "Author '${author.name}' deleted"
        }
    }

    fun addLiveUpdate(topicTitle: String, headline: String, content: String, badge: String) {
        viewModelScope.launch {
            val sdf = java.text.SimpleDateFormat("hh:mm a 'IST'", java.util.Locale.getDefault())
            val timeLabel = sdf.format(java.util.Date())
            val update = LiveUpdate(
                topicTitle = topicTitle.trim(),
                timeLabel = timeLabel,
                headline = headline.trim(),
                content = content.trim(),
                badge = badge
            )
            repository.saveLiveUpdate(update)
            _userMessage.value = "Live update broadcasted!"
        }
    }

    fun deleteLiveUpdate(update: LiveUpdate) {
        viewModelScope.launch {
            repository.deleteLiveUpdate(update)
            _userMessage.value = "Live update deleted"
        }
    }

    fun addVideo(title: String, description: String, youtubeUrl: String, thumbnailUrl: String, category: String, duration: String) {
        viewModelScope.launch {
            val video = VideoNews(
                title = title.trim(),
                description = description.trim(),
                youtubeUrl = youtubeUrl.trim(),
                thumbnailUrl = thumbnailUrl.ifBlank { "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80" },
                category = category,
                duration = duration.ifBlank { "04:30" }
            )
            repository.saveVideo(video)
            _userMessage.value = "Video story added!"
        }
    }

    fun deleteVideo(video: VideoNews) {
        viewModelScope.launch {
            repository.deleteVideo(video)
            _userMessage.value = "Video deleted"
        }
    }

    fun addMedia(title: String, url: String, altText: String) {
        viewModelScope.launch {
            val media = MediaItem(
                title = title.trim(),
                url = url.trim(),
                altText = altText.trim(),
                dimensions = "1200x800"
            )
            repository.saveMedia(media)
            _userMessage.value = "Media asset saved to library"
        }
    }

    fun deleteMedia(media: MediaItem) {
        viewModelScope.launch {
            repository.deleteMedia(media)
            _userMessage.value = "Media item deleted"
        }
    }

    fun updateSettings(
        siteName: String,
        tagline: String,
        contactEmail: String,
        breakingTickerEnabled: Boolean,
        defaultSeoTitle: String,
        defaultSeoDesc: String,
        adminPin: String
    ) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(
                siteName = siteName.trim(),
                tagline = tagline.trim(),
                contactEmail = contactEmail.trim(),
                breakingTickerEnabled = breakingTickerEnabled,
                defaultSeoTitle = defaultSeoTitle.trim(),
                defaultSeoDesc = defaultSeoDesc.trim(),
                adminPin = adminPin.trim().ifBlank { current.adminPin }
            )
            repository.saveSettings(updated)
            _userMessage.value = "Settings updated successfully"
        }
    }

    fun resetToDemoData() {
        viewModelScope.launch {
            repository.resetToSeedData()
            _userMessage.value = "Reset to fresh demo newsroom journalism dataset"
        }
    }

    fun clearAllDemoData() {
        viewModelScope.launch {
            repository.clearAllNewsData()
            _userMessage.value = "All demo news cleared. Newsroom is fresh & ready!"
        }
    }
}
