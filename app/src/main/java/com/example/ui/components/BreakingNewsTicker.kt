package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Article
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun BreakingNewsTicker(
    breakingArticles: List<Article>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    if (breakingArticles.isEmpty()) return

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(breakingArticles.size) {
        if (breakingArticles.isNotEmpty()) {
            while (true) {
                delay(5000L)
                currentIndex = (currentIndex + 1) % breakingArticles.size
            }
        }
    }

    val currentArticle = breakingArticles.getOrNull(currentIndex) ?: breakingArticles.first()

    Surface(
        color = BrandOrange,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onArticleClick(currentArticle) }
            .testTag("breaking_news_ticker")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // High contrast Black badge with BREAKING text
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BREAKING",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Animated headline text in dark bold contrast
            AnimatedContent(
                targetState = currentArticle,
                transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn()) togetherWith
                    (slideOutVertically { height -> -height } + fadeOut())
                },
                modifier = Modifier.weight(1f),
                label = "breaking_headline"
            ) { targetArticle ->
                Text(
                    text = targetArticle.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.NavigateNext,
                contentDescription = "Read Breaking News",
                tint = Color.Black,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
