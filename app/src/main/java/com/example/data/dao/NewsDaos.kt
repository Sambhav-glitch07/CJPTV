package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE status = 'PUBLISHED' ORDER BY publishedAt DESC")
    fun getPublishedArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE status = 'DRAFT' ORDER BY updatedAt DESC")
    fun getDraftArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE isBreaking = 1 AND status = 'PUBLISHED' ORDER BY publishedAt DESC")
    fun getBreakingArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE isFeatured = 1 AND status = 'PUBLISHED' ORDER BY publishedAt DESC LIMIT 5")
    fun getFeaturedArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE status = 'PUBLISHED' ORDER BY viewCount DESC LIMIT 8")
    fun getTrendingArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE (categorySlug = :categorySlug OR category = :categorySlug) AND status = 'PUBLISHED' ORDER BY publishedAt DESC")
    fun getArticlesByCategory(categorySlug: String): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE slug = :slug LIMIT 1")
    suspend fun getArticleBySlug(slug: String): Article?

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun getArticleById(id: Long): Article?

    @Query("SELECT * FROM articles WHERE (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR authorName LIKE '%' || :query || '%') AND status = 'PUBLISHED' ORDER BY publishedAt DESC")
    fun searchArticles(query: String): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE categorySlug = :categorySlug AND id != :currentId AND status = 'PUBLISHED' ORDER BY publishedAt DESC LIMIT 4")
    fun getRelatedArticles(categorySlug: String, currentId: Long): Flow<List<Article>>

    @Query("UPDATE articles SET viewCount = viewCount + 1 WHERE id = :id")
    suspend fun incrementViewCount(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY sortOrder ASC")
    fun getAllCategories(): Flow<List<NewsCategory>>

    @Query("SELECT * FROM categories WHERE slug = :slug LIMIT 1")
    suspend fun getCategoryBySlug(slug: String): NewsCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: NewsCategory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<NewsCategory>)

    @Update
    suspend fun updateCategory(category: NewsCategory)

    @Delete
    suspend fun deleteCategory(category: NewsCategory)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM categories")
    suspend fun clearAll()
}

@Dao
interface AuthorDao {
    @Query("SELECT * FROM authors ORDER BY name ASC")
    fun getAllAuthors(): Flow<List<Author>>

    @Query("SELECT * FROM authors WHERE slug = :slug LIMIT 1")
    suspend fun getAuthorBySlug(slug: String): Author?

    @Query("SELECT * FROM authors WHERE id = :id LIMIT 1")
    suspend fun getAuthorById(id: Long): Author?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: Author): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(authors: List<Author>)

    @Update
    suspend fun updateAuthor(author: Author)

    @Delete
    suspend fun deleteAuthor(author: Author)

    @Query("DELETE FROM authors WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM authors")
    suspend fun clearAll()
}

@Dao
interface LiveUpdateDao {
    @Query("SELECT * FROM live_updates ORDER BY timestamp DESC")
    fun getAllLiveUpdates(): Flow<List<LiveUpdate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveUpdate(update: LiveUpdate): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(updates: List<LiveUpdate>)

    @Delete
    suspend fun deleteLiveUpdate(update: LiveUpdate)

    @Query("DELETE FROM live_updates WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM live_updates")
    suspend fun clearAll()
}

@Dao
interface VideoNewsDao {
    @Query("SELECT * FROM video_news ORDER BY publishedAt DESC")
    fun getAllVideos(): Flow<List<VideoNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoNews): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoNews>)

    @Delete
    suspend fun deleteVideo(video: VideoNews)

    @Query("DELETE FROM video_news WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM video_news")
    suspend fun clearAll()
}

@Dao
interface MediaItemDao {
    @Query("SELECT * FROM media_items ORDER BY uploadedAt DESC")
    fun getAllMedia(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE title LIKE '%' || :query || '%' OR altText LIKE '%' || :query || '%' ORDER BY uploadedAt DESC")
    fun searchMedia(query: String): Flow<List<MediaItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: MediaItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mediaList: List<MediaItem>)

    @Delete
    suspend fun deleteMedia(media: MediaItem)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM media_items")
    suspend fun clearAll()
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM admin_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<AdminSettings?>

    @Query("SELECT * FROM admin_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): AdminSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: AdminSettings)
}
