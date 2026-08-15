package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsCategory
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CJPTVHeader(
    siteName: String = "CJPTV.in",
    tagline: String = "YOUR NEWS. YOUR VOICE.",
    categories: List<NewsCategory>,
    selectedCategorySlug: String?,
    onCategorySelected: (String?) -> Unit,
    onSearchClick: () -> Unit,
    onLiveClick: () -> Unit,
    onVideosClick: () -> Unit,
    onMenuClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulsing live dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_live")
    val livePulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "live_alpha"
    )

    val todayFormatted = remember {
        val sdf = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH)
        sdf.format(Date())
    }

    Surface(
        color = DarkHeader,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Website Top Utility Bar (Website Header standard)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🌐 cjptv.in",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandOrange
                    )
                    Text(
                        text = "  |  $todayFormatted",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.5.sp,
                        color = TextMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "EDITION: PATNA / NATIONAL",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "☀️ 32°C",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }

            // Main Website Masthead Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Website Hamburger Menu Button
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant)
                        .border(1.dp, DarkCardBorder, CircleShape)
                        .testTag("header_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Website Navigation Menu",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Center: Big Bold Website Digital News Masthead
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onHomeClick() }
                        .testTag("brand_logo_header")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CJPTV",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = Color.White
                        )
                        Text(
                            text = ".IN",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = BrandOrange
                        )
                    }
                    Text(
                        text = tagline.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.6.sp,
                        color = TextMuted
                    )
                }

                // Right: Action Icons (Search, Live Updates) - No Upload Button here
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Search Button
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .border(1.dp, DarkCardBorder, CircleShape)
                            .testTag("header_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search News",
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Elegant Live Pill
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = BreakingRed,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onLiveClick() }
                            .testTag("header_live_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .alpha(livePulseAlpha)
                                    .background(Color.White)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            // Categories horizontal bar with underline indicators (News Website Navigation Bar)
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkHeader)
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "Top Stories" tab
                val isAllSelected = selectedCategorySlug == null
                CategoryNavItem(
                    title = "Top Stories",
                    isSelected = isAllSelected,
                    onClick = { onCategorySelected(null) },
                    testTag = "category_chip_all"
                )

                // Category Tabs
                categories.forEach { category ->
                    val isSelected = selectedCategorySlug == category.slug
                    CategoryNavItem(
                        title = category.name,
                        isSelected = isSelected,
                        onClick = { onCategorySelected(category.slug) },
                        testTag = "category_chip_${category.slug}"
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryNavItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) BrandOrange else TextMuted,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(BrandOrange)
            )
        } else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
