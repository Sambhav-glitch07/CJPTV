package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.data.model.NewsCategory
import com.example.data.model.VideoNews
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    publishedArticles: List<Article>,
    breakingArticles: List<Article>,
    featuredArticles: List<Article>,
    trendingArticles: List<Article>,
    categories: List<NewsCategory>,
    videos: List<VideoNews>,
    selectedCategorySlug: String?,
    onCategorySelected: (String?) -> Unit,
    onArticleClick: (Article) -> Unit,
    onVideoClick: (VideoNews) -> Unit,
    onShareArticle: (Article) -> Unit,
    onSeeAllVideos: () -> Unit,
    onAboutClick: () -> Unit,
    onLegalClick: (String) -> Unit,
    onSitemapClick: () -> Unit,
    onSecretStaffAccessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredArticles = remember(publishedArticles, selectedCategorySlug) {
        if (selectedCategorySlug == null) {
            publishedArticles
        } else {
            publishedArticles.filter {
                it.categorySlug.equals(selectedCategorySlug, ignoreCase = true) ||
                it.category.equals(selectedCategorySlug, ignoreCase = true)
            }
        }
    }

    val heroArticle = remember(featuredArticles, publishedArticles) {
        featuredArticles.firstOrNull() ?: publishedArticles.firstOrNull()
    }

    val remainingArticles = remember(filteredArticles, heroArticle) {
        if (selectedCategorySlug == null && heroArticle != null) {
            filteredArticles.filter { it.id != heroArticle.id }
        } else {
            filteredArticles
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
                .testTag("home_screen_feed")
        ) {
            // Breaking News Ticker
            if (breakingArticles.isNotEmpty()) {
                item {
                    BreakingNewsTicker(
                        breakingArticles = breakingArticles,
                        onArticleClick = onArticleClick
                    )
                }
            }

            // Hero Lead Article (Only when on Home / All tab)
            if (selectedCategorySlug == null && heroArticle != null) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        HeroArticleCard(
                            article = heroArticle,
                            onArticleClick = onArticleClick
                        )
                    }
                }
            }

            // Category Header when filtered
            if (selectedCategorySlug != null) {
                item {
                    Surface(
                        color = DarkSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "CATEGORY: ${selectedCategorySlug.uppercase()}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandOrange,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "${filteredArticles.size} stories published",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                            TextButton(onClick = { onCategorySelected(null) }) {
                                Text("Show All News", color = BrandOrange, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Trending News Carousel (when on Home tab)
            if (selectedCategorySlug == null && trendingArticles.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(BrandOrange)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TRENDING & VIRAL STORIES",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandOrange,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(trendingArticles) { index, article ->
                                TrendingArticleCard(
                                    rank = index + 1,
                                    article = article,
                                    onArticleClick = onArticleClick
                                )
                            }
                        }
                    }
                }
            }

            // Video Highlights Carousel (when on Home tab)
            if (selectedCategorySlug == null && videos.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(BreakingRed)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "VIDEO DESK & GROUND REPORTS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp
                                )
                            }

                            TextButton(onClick = onSeeAllVideos) {
                                Text("See All Videos", color = BrandOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(videos) { video ->
                                VideoNewsCard(
                                    video = video,
                                    onVideoClick = onVideoClick
                                )
                            }
                        }
                    }
                }
            }

            // Latest News Feed Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedCategorySlug == null) "LATEST STORIES & SPECIAL REPORTS" else "STORIES IN ${selectedCategorySlug.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Standard Articles List
            if (remainingArticles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Article,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No news stories in this section yet.",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                items(remainingArticles, key = { it.id }) { article ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        StandardArticleCard(
                            article = article,
                            onArticleClick = onArticleClick,
                            onShareClick = onShareArticle
                        )
                    }
                }
            }

            // Website Footer at bottom of feed
            item {
                Spacer(modifier = Modifier.height(20.dp))
                FooterView(
                    onCategoryClick = onCategorySelected,
                    onAboutClick = onAboutClick,
                    onLegalClick = onLegalClick,
                    onSitemapClick = onSitemapClick,
                    onStaffPortalClick = onSecretStaffAccessClick
                )
            }
        }
    }
}
