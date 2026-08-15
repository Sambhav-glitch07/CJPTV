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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsCategory
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebsiteMenuSheet(
    categories: List<NewsCategory>,
    selectedCategorySlug: String?,
    onCategoryClick: (String?) -> Unit,
    onVideosClick: () -> Unit,
    onLiveClick: () -> Unit,
    onSearchClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLegalClick: (String) -> Unit,
    onSitemapClick: () -> Unit,
    onSecretStaffAccessClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkHeader,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = DarkCardBorder)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Masthead in Drawer
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CJPTV",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = ".IN",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = BrandOrange
                            )
                        }
                        Text(
                            text = "YOUR NEWS. YOUR VOICE.",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted,
                            letterSpacing = 1.4.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Menu",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            item {
                HorizontalDivider(color = DarkCardBorder)
            }

            // Quick Nav Shortcuts
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WebsiteQuickNavCard(
                        title = "Live Ticker",
                        icon = Icons.Default.Sensors,
                        accentColor = BreakingRed,
                        onClick = {
                            onDismiss()
                            onLiveClick()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    WebsiteQuickNavCard(
                        title = "Video Desk",
                        icon = Icons.Default.PlayCircleFilled,
                        accentColor = BrandOrange,
                        onClick = {
                            onDismiss()
                            onVideosClick()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    WebsiteQuickNavCard(
                        title = "Search",
                        icon = Icons.Default.Search,
                        accentColor = Color(0xFF4CC9F0),
                        onClick = {
                            onDismiss()
                            onSearchClick()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // News Sections
            item {
                Text(
                    text = "EDITORIAL SECTIONS & BEATS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                ) {
                    // All / Top Stories
                    WebsiteSectionRow(
                        title = "Top Stories & National Headlines",
                        subtitle = "Leading news from Bihar and across India",
                        icon = Icons.Default.Newspaper,
                        isSelected = selectedCategorySlug == null,
                        onClick = {
                            onDismiss()
                            onCategoryClick(null)
                        }
                    )

                    HorizontalDivider(color = DarkCardBorder.copy(alpha = 0.5f))

                    categories.forEachIndexed { index, cat ->
                        WebsiteSectionRow(
                            title = cat.name,
                            subtitle = cat.description.ifBlank { "Latest ${cat.name} news coverage" },
                            icon = when (cat.slug.lowercase()) {
                                "bihar" -> Icons.Default.LocationOn
                                "india" -> Icons.Default.Flag
                                "politics" -> Icons.Default.AccountBalance
                                "business" -> Icons.Default.TrendingUp
                                "technology" -> Icons.Default.Memory
                                "sports" -> Icons.Default.SportsCricket
                                "world" -> Icons.Default.Public
                                else -> Icons.Default.Article
                            },
                            isSelected = selectedCategorySlug == cat.slug,
                            onClick = {
                                onDismiss()
                                onCategoryClick(cat.slug)
                            }
                        )
                        if (index < categories.size - 1) {
                            HorizontalDivider(color = DarkCardBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            // Digital Editions
            item {
                Text(
                    text = "DIGITAL EDITIONS & SERVICES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Patna (State HQ)", "New Delhi", "Muzaffarpur", "Gaya").forEach { ed ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = ed,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.5.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Information & Legal
            item {
                Text(
                    text = "ABOUT & GOVERNANCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                ) {
                    WebsiteSimpleLink(
                        title = "About CJPTV.in & Founder Sambhav Gupta",
                        icon = Icons.Default.Info,
                        onClick = {
                            onDismiss()
                            onAboutClick()
                        }
                    )
                    HorizontalDivider(color = DarkCardBorder.copy(alpha = 0.5f))
                    WebsiteSimpleLink(
                        title = "Sitemap & Archive",
                        icon = Icons.Default.AccountTree,
                        onClick = {
                            onDismiss()
                            onSitemapClick()
                        }
                    )
                    HorizontalDivider(color = DarkCardBorder.copy(alpha = 0.5f))
                    WebsiteSimpleLink(
                        title = "Editorial Policies & Code of Ethics",
                        icon = Icons.Default.Gavel,
                        onClick = {
                            onDismiss()
                            onLegalClick("terms")
                        }
                    )
                    HorizontalDivider(color = DarkCardBorder.copy(alpha = 0.5f))
                    WebsiteSimpleLink(
                        title = "Privacy Policy & Disclaimers",
                        icon = Icons.Default.Security,
                        onClick = {
                            onDismiss()
                            onLegalClick("privacy")
                        }
                    )
                }
            }

            // Secret Editorial Staff Access Link at Bottom of Menu
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDismiss()
                            onSecretStaffAccessClick()
                        }
                        .testTag("secret_staff_menu_link")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Staff Access",
                            tint = TextMuted.copy(alpha = 0.6f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Editorial Desk / Staff Portal",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            color = TextMuted.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WebsiteQuickNavCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = DarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun WebsiteSectionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) BrandOrange else DarkBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else BrandOrangeLight,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) BrandOrange else Color.White,
                fontSize = 13.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.5.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun WebsiteSimpleLink(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            fontSize = 12.5.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}
