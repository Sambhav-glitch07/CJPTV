package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.ui.theme.*

@Composable
fun FooterView(
    onCategoryClick: (String) -> Unit,
    onAboutClick: () -> Unit,
    onLegalClick: (String) -> Unit,
    onSitemapClick: () -> Unit,
    onStaffPortalClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DarkHeader,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Brand & Tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("footer_brand")
            ) {
                Text(
                    text = "CJPTV",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    color = Color.White
                )
                Text(
                    text = ".IN",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    color = BrandOrange
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "YOUR NEWS. YOUR VOICE.",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.5.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Founder Highlight
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DarkSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAboutClick() }
                    .testTag("footer_founder_link")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = BrandOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Founded by Sambhav Gupta — Independent Digital Journalism",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View Profile",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories Grid Links
            Text(
                text = "TOP CATEGORIES",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            val cats = listOf("India", "Bihar", "World", "Politics", "Business", "Sports", "Technology", "Health")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cats.take(4).forEach { cat ->
                    Text(
                        text = cat,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { onCategoryClick(cat.lowercase()) }
                            .padding(vertical = 4.dp)
                    )
                    if (cat != cats[3]) Text("•", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cats.drop(4).forEach { cat ->
                    Text(
                        text = cat,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { onCategoryClick(cat.lowercase()) }
                            .padding(vertical = 4.dp)
                    )
                    if (cat != cats.last()) Text("•", color = TextMuted, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = DarkCardBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Legal & Info Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onAboutClick() }
                )
                Text("•", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = "Privacy",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onLegalClick("privacy") }
                )
                Text("•", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = "Terms",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onLegalClick("terms") }
                )
                Text("•", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = "Contact",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onLegalClick("contact") }
                )
                Text("•", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = "Sitemap",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandOrangeLight,
                    modifier = Modifier.clickable { onSitemapClick() }
                )

                if (onStaffPortalClick != null) {
                    Text("•", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "Staff Desk",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted.copy(alpha = 0.5f),
                        fontSize = 9.sp,
                        modifier = Modifier.clickable { onStaffPortalClick() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Copyright
            Text(
                text = "© 2026 CJPTV.in. All Rights Reserved. Independent Digital Journalism.",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
