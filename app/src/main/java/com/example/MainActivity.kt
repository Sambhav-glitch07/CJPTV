package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.data.model.VideoNews
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.NewsViewModel

enum class ScreenTab {
    HOME,
    VIDEOS,
    LIVE,
    ADMIN,
    SEARCH,
    ARTICLE_DETAIL
}

class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewsAppTheme {
                MainAppContent(viewModel = newsViewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: NewsViewModel
) {
    val context = LocalContext.current

    val publishedArticles by viewModel.publishedArticles.collectAsState()
    val allArticles by viewModel.allArticles.collectAsState()
    val breakingArticles by viewModel.breakingArticles.collectAsState()
    val featuredArticles by viewModel.featuredArticles.collectAsState()
    val trendingArticles by viewModel.trendingArticles.collectAsState()
    val categories by viewModel.allCategories.collectAsState()
    val authors by viewModel.allAuthors.collectAsState()
    val liveUpdates by viewModel.liveUpdates.collectAsState()
    val videos by viewModel.allVideos.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val currentArticle by viewModel.currentArticle.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    // Navigation State
    var currentTab by remember { mutableStateOf(ScreenTab.HOME) }
    var selectedCategorySlug by remember { mutableStateOf<String?>(null) }
    var adminInitialTab by remember { mutableIntStateOf(0) }

    // Website Menu Sheet State
    var showWebsiteMenu by remember { mutableStateOf(false) }

    // Dialogs
    var showAboutDialog by remember { mutableStateOf(false) }
    var showLegalDialog by remember { mutableStateOf<String?>(null) }
    var showSitemapDialog by remember { mutableStateOf(false) }

    // User message toasts
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearUserMessage()
        }
    }

    // Share Helper
    val onShareArticle: (Article) -> Unit = { article ->
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, article.title)
            putExtra(Intent.EXTRA_TEXT, "${article.title}\n\nRead more on CJPTV.in: https://cjptv.in/news/${article.slug}")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share News Story via"))
    }

    // Video Click Helper
    val onVideoClick: (VideoNews) -> Unit = { video ->
        Toast.makeText(context, "Playing Video: ${video.title}", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBg,
        topBar = {
            // Website Masthead Header on Home screen
            if (currentTab == ScreenTab.HOME) {
                CJPTVHeader(
                    siteName = settings.siteName,
                    tagline = settings.tagline,
                    categories = categories,
                    selectedCategorySlug = selectedCategorySlug,
                    onCategorySelected = { slug -> selectedCategorySlug = slug },
                    onSearchClick = { currentTab = ScreenTab.SEARCH },
                    onLiveClick = { currentTab = ScreenTab.LIVE },
                    onVideosClick = { currentTab = ScreenTab.VIDEOS },
                    onMenuClick = { showWebsiteMenu = true },
                    onHomeClick = {
                        selectedCategorySlug = null
                        currentTab = ScreenTab.HOME
                    }
                )
            }
        },
        bottomBar = {
            // Clean News Website Navigation Bar
            if (currentTab != ScreenTab.SEARCH && currentTab != ScreenTab.ARTICLE_DETAIL) {
                Surface(
                    color = DarkNavBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem(
                            icon = Icons.Default.Home,
                            label = "Home",
                            isSelected = currentTab == ScreenTab.HOME,
                            onClick = {
                                selectedCategorySlug = null
                                currentTab = ScreenTab.HOME
                            },
                            testTag = "nav_tab_home"
                        )

                        BottomNavItem(
                            icon = Icons.Default.PlayCircleFilled,
                            label = "Videos",
                            isSelected = currentTab == ScreenTab.VIDEOS,
                            onClick = { currentTab = ScreenTab.VIDEOS },
                            testTag = "nav_tab_videos"
                        )

                        BottomNavItem(
                            icon = Icons.Default.Sensors,
                            label = "Live News",
                            isSelected = currentTab == ScreenTab.LIVE,
                            hasLiveBadge = true,
                            onClick = { currentTab = ScreenTab.LIVE },
                            testTag = "nav_tab_live"
                        )

                        BottomNavItem(
                            icon = Icons.Default.Menu,
                            label = "Sections",
                            isSelected = showWebsiteMenu,
                            onClick = { showWebsiteMenu = true },
                            testTag = "nav_tab_menu"
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBg)
        ) {
            when (currentTab) {
                ScreenTab.HOME -> {
                    HomeScreen(
                        publishedArticles = publishedArticles,
                        breakingArticles = breakingArticles,
                        featuredArticles = featuredArticles,
                        trendingArticles = trendingArticles,
                        categories = categories,
                        videos = videos,
                        selectedCategorySlug = selectedCategorySlug,
                        onCategorySelected = { slug -> selectedCategorySlug = slug },
                        onArticleClick = { article ->
                            viewModel.loadArticleBySlug(article.slug)
                            currentTab = ScreenTab.ARTICLE_DETAIL
                        },
                        onVideoClick = onVideoClick,
                        onShareArticle = onShareArticle,
                        onSeeAllVideos = { currentTab = ScreenTab.VIDEOS },
                        onAboutClick = { showAboutDialog = true },
                        onLegalClick = { section -> showLegalDialog = section },
                        onSitemapClick = { showSitemapDialog = true },
                        onSecretStaffAccessClick = {
                            adminInitialTab = 0
                            currentTab = ScreenTab.ADMIN
                        }
                    )
                }

                ScreenTab.VIDEOS -> {
                    VideosScreen(
                        videos = videos,
                        onVideoClick = onVideoClick,
                        onBackClick = { currentTab = ScreenTab.HOME }
                    )
                }

                ScreenTab.LIVE -> {
                    LiveNewsScreen(
                        liveUpdates = liveUpdates,
                        isAdminLoggedIn = isAdminLoggedIn,
                        onAddLiveUpdate = { topic, headline, content, badge ->
                            viewModel.addLiveUpdate(topic, headline, content, badge)
                        },
                        onDeleteLiveUpdate = { update ->
                            viewModel.deleteLiveUpdate(update)
                        },
                        onBackClick = { currentTab = ScreenTab.HOME }
                    )
                }

                ScreenTab.ADMIN -> {
                    AdminScreen(
                        articles = allArticles,
                        categories = categories,
                        authors = authors,
                        settings = settings,
                        isAdminLoggedIn = isAdminLoggedIn,
                        initialTab = adminInitialTab,
                        onLogin = { pin -> viewModel.loginAdmin(pin) },
                        onLogout = { viewModel.logoutAdmin() },
                        onSaveArticle = { id, title, desc, content, cat, catSlug, authorId, authorName, authorRole, imgUrl, imgCaption, isBreaking, isFeatured, isTrending, status ->
                            viewModel.saveArticle(
                                id = id,
                                title = title,
                                shortDesc = desc,
                                content = content,
                                category = cat,
                                categorySlug = catSlug,
                                authorId = authorId,
                                authorName = authorName,
                                authorRole = authorRole,
                                imageUrl = imgUrl,
                                imageCaption = imgCaption,
                                isBreaking = isBreaking,
                                isFeatured = isFeatured,
                                isTrending = isTrending,
                                status = status
                            )
                        },
                        onDeleteArticle = { article -> viewModel.deleteArticle(article) },
                        onToggleBreaking = { article -> viewModel.toggleBreaking(article) },
                        onToggleFeatured = { article -> viewModel.toggleFeatured(article) },
                        onToggleTrending = { article -> viewModel.toggleTrending(article) },
                        onAddCategory = { name, slug, color, desc -> viewModel.addCategory(name, slug, color, desc) },
                        onDeleteCategory = { cat -> viewModel.deleteCategory(cat) },
                        onResetDemoData = { viewModel.resetToDemoData() },
                        onBackClick = { currentTab = ScreenTab.HOME }
                    )
                }

                ScreenTab.SEARCH -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        searchResults = searchResults,
                        categories = categories,
                        onQueryChange = { q -> viewModel.setSearchQuery(q) },
                        onArticleClick = { article ->
                            viewModel.loadArticleBySlug(article.slug)
                            currentTab = ScreenTab.ARTICLE_DETAIL
                        },
                        onBackClick = { currentTab = ScreenTab.HOME }
                    )
                }

                ScreenTab.ARTICLE_DETAIL -> {
                    currentArticle?.let { article ->
                        ArticleDetailScreen(
                            article = article,
                            relatedArticles = publishedArticles.filter { it.id != article.id },
                            onArticleClick = { nextArticle ->
                                viewModel.loadArticleBySlug(nextArticle.slug)
                            },
                            onBackClick = { currentTab = ScreenTab.HOME },
                            onShareClick = onShareArticle
                        )
                    } ?: run {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = BrandOrange)
                        }
                    }
                }
            }
        }
    }

    // Full Website Sections Menu Sheet
    if (showWebsiteMenu) {
        WebsiteMenuSheet(
            categories = categories,
            selectedCategorySlug = selectedCategorySlug,
            onCategoryClick = { slug ->
                selectedCategorySlug = slug
                currentTab = ScreenTab.HOME
                showWebsiteMenu = false
            },
            onVideosClick = {
                currentTab = ScreenTab.VIDEOS
                showWebsiteMenu = false
            },
            onLiveClick = {
                currentTab = ScreenTab.LIVE
                showWebsiteMenu = false
            },
            onSearchClick = {
                currentTab = ScreenTab.SEARCH
                showWebsiteMenu = false
            },
            onAboutClick = {
                showWebsiteMenu = false
                showAboutDialog = true
            },
            onLegalClick = { sec ->
                showWebsiteMenu = false
                showLegalDialog = sec
            },
            onSitemapClick = {
                showWebsiteMenu = false
                showSitemapDialog = true
            },
            onSecretStaffAccessClick = {
                showWebsiteMenu = false
                adminInitialTab = 0
                currentTab = ScreenTab.ADMIN
            },
            onDismiss = { showWebsiteMenu = false }
        )
    }

    // About Founder / Portal Dialog
    if (showAboutDialog) {
        AboutCJPTVDialog(
            onDismiss = { showAboutDialog = false }
        )
    }

    // Legal / Policy Dialog
    showLegalDialog?.let { section ->
        LegalDialog(
            section = section,
            onDismiss = { showLegalDialog = null }
        )
    }

    // Sitemap Dialog
    if (showSitemapDialog) {
        SitemapDialog(
            categories = categories,
            articles = publishedArticles,
            onSelectArticle = { article ->
                viewModel.loadArticleBySlug(article.slug)
                currentTab = ScreenTab.ARTICLE_DETAIL
                showSitemapDialog = false
            },
            onDismiss = { showSitemapDialog = false }
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    hasLiveBadge: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) BrandOrange else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
            if (hasLiveBadge) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(BreakingRed)
                )
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp,
            color = if (isSelected) BrandOrange else TextMuted
        )
    }
}
