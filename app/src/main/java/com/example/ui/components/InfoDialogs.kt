package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.*

@Composable
fun AboutCJPTVDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = BrandOrange,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "About CJPTV.in",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(BrandOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SG",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sambhav Gupta",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Founder & Editor-in-Chief",
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandOrangeLight
                            )
                        }
                    }
                }

                Text(
                    text = "CJPTV.in is an independent digital news platform committed to unbiased, ground-level journalism covering Bihar, National affairs, Politics, Economy, Science, and World events.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )

                Text(
                    text = "Our motto: 'Your News. Your Voice.' reflects our unyielding commitment to give voice to the citizens and bring truth without corporate censorship.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
            ) {
                Text("Close", color = Color.White)
            }
        }
    )
}

@Composable
fun LegalDialog(
    section: String,
    onDismiss: () -> Unit
) {
    val title = when (section) {
        "privacy" -> "Privacy Policy"
        "terms" -> "Terms of Service"
        "disclaimer" -> "Editorial Disclaimer"
        "contact" -> "Contact CJPTV Editorial"
        else -> "Information"
    }

    val content = when (section) {
        "privacy" -> "CJPTV.in respects user privacy. We do not sell personal data to third parties. We collect minimal telemetry strictly to deliver personalized news feeds and editorial caching."
        "terms" -> "All content published on CJPTV.in, including text, photographs, and video bulletins, is copyrighted. Unauthorized scraping or republication is strictly prohibited under applicable digital laws."
        "disclaimer" -> "CJPTV.in adheres to the highest fact-checking standards. Opinion pieces reflect the views of respective authors and not necessarily the editorial board. We promptly publish corrections for verified factual disputes."
        "contact" -> "CJPTV Editorial Bureau:\nEmail: editor@cjptv.in / desk@cjptv.in\nHeadquarters: Patna / New Delhi, India\nHelpline: +91 612 220 9800"
        else -> "CJPTV.in Digital Newsroom"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
            ) {
                Text("Understood", color = Color.White)
            }
        }
    )
}

@Composable
fun SitemapDialog(
    categories: List<NewsCategory>,
    articles: List<Article>,
    onSelectArticle: (Article) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountTree,
                    contentDescription = null,
                    tint = BrandOrange,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CJPTV Index & Sitemap",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                item {
                    Text(
                        text = "ACTIVE EDITORIAL BEATS (${categories.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(categories) { cat ->
                    Text(
                        text = "• /${cat.slug} — ${cat.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "INDEXED STORIES (${articles.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandOrange,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(articles) { article ->
                    Text(
                        text = "• ${article.title}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        maxLines = 1,
                        modifier = Modifier
                            .clickable {
                                onSelectArticle(article)
                                onDismiss()
                            }
                            .padding(vertical = 2.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
            ) {
                Text("Close", color = Color.White)
            }
        }
    )
}
