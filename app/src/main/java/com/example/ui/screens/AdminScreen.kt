package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun AdminScreen(
    isAdminLoggedIn: Boolean,
    articles: List<Article>,
    categories: List<NewsCategory>,
    authors: List<Author>,
    settings: AdminSettings,
    onLogin: (String) -> Boolean,
    onLogout: () -> Unit,
    onSaveArticle: (
        id: Long,
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
        status: String
    ) -> Unit,
    onDeleteArticle: (Article) -> Unit,
    onToggleBreaking: (Article) -> Unit,
    onToggleFeatured: (Article) -> Unit,
    onToggleTrending: (Article) -> Unit,
    onAddCategory: (String, String, String, String) -> Unit,
    onDeleteCategory: (NewsCategory) -> Unit,
    onResetDemoData: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialTab: Int = 0
) {
    val context = LocalContext.current
    var adminPinInput by remember { mutableStateOf("") }
    var isPinVisible by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    var articleToEdit by remember { mutableStateOf<Article?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .testTag("admin_screen")
    ) {
        // Header
        Surface(
            color = DarkHeader,
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                            .testTag("admin_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CJPTV",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = " Admin CMS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandOrange
                            )
                        }
                        Text(
                            text = if (isAdminLoggedIn) "Editorial Publishing Desk" else "News Upload & Management Portal",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                if (isAdminLoggedIn) {
                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("admin_logout_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = BreakingRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Exit CMS", color = BreakingRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (!isAdminLoggedIn) {
            // Login Gate with Secure PIN Entry
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BrandOrangeContainer)
                                .border(1.dp, BrandOrange.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = BrandOrange,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Editorial Security Gate",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Please enter the authorized security PIN to access the CJPTV news publishing desk.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = adminPinInput,
                            onValueChange = { adminPinInput = it },
                            label = { Text("Security PIN") },
                            placeholder = { Text("••••••") },
                            visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { isPinVisible = !isPinVisible }) {
                                    Icon(
                                        imageVector = if (isPinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle PIN visibility",
                                        tint = TextMuted
                                    )
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandOrange,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = DarkSurfaceVariant,
                                unfocusedContainerColor = DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_pin_input")
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                onLogin(adminPinInput)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("admin_login_submit_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Verify & Access Desk",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            // Logged in CMS View with Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkHeader,
                contentColor = BrandOrange,
                divider = { HorizontalDivider(color = DarkCardBorder) }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        articleToEdit = null
                    },
                    text = { Text("Articles (${articles.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (articleToEdit != null) "✏️ Edit Story" else "+ Upload News",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) BrandOrange else Color.White
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Categories", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Settings", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                0 -> {
                    // Articles Manager
                    ArticlesManagerView(
                        articles = articles,
                        onAddNewClick = {
                            articleToEdit = null
                            selectedTab = 1
                        },
                        onEditArticle = { article ->
                            articleToEdit = article
                            selectedTab = 1
                        },
                        onToggleBreaking = onToggleBreaking,
                        onToggleFeatured = onToggleFeatured,
                        onToggleTrending = onToggleTrending,
                        onDelete = onDeleteArticle
                    )
                }
                1 -> {
                    // Create / Edit Article Form
                    CreateOrEditArticleView(
                        existingArticle = articleToEdit,
                        categories = categories,
                        authors = authors,
                        onPublish = { id, title, shortDesc, content, cat, catSlug, aId, aName, aRole, img, caption, isB, isF, isT ->
                            onSaveArticle(id, title, shortDesc, content, cat, catSlug, aId, aName, aRole, img, caption, isB, isF, isT, "PUBLISHED")
                            Toast.makeText(context, "Story published successfully to CJPTV.in!", Toast.LENGTH_SHORT).show()
                            articleToEdit = null
                            selectedTab = 0
                        },
                        onCancelEdit = {
                            articleToEdit = null
                            selectedTab = 0
                        }
                    )
                }
                2 -> {
                    // Categories Manager
                    CategoriesManagerView(
                        categories = categories,
                        onAddCategory = onAddCategory,
                        onDeleteCategory = onDeleteCategory
                    )
                }
                3 -> {
                    // Settings & Demo Reset
                    SettingsManagerView(
                        settings = settings,
                        onResetDemoData = onResetDemoData
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticlesManagerView(
    articles: List<Article>,
    onAddNewClick: () -> Unit,
    onEditArticle: (Article) -> Unit,
    onToggleBreaking: (Article) -> Unit,
    onToggleFeatured: (Article) -> Unit,
    onToggleTrending: (Article) -> Unit,
    onDelete: (Article) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = BrandOrangeContainer.copy(alpha = 0.3f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandOrange.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "News Publishing Desk",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Upload new stories or toggle Breaking/Hero banners.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = onAddNewClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("admin_upload_new_story_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Upload Story", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(articles, key = { it.id }) { article ->
            AdminArticleRowCard(
                article = article,
                onEdit = { onEditArticle(article) },
                onToggleBreaking = { onToggleBreaking(article) },
                onToggleFeatured = { onToggleFeatured(article) },
                onToggleTrending = { onToggleTrending(article) },
                onDelete = { onDelete(article) }
            )
        }
    }
}

@Composable
private fun AdminArticleRowCard(
    article: Article,
    onEdit: () -> Unit,
    onToggleBreaking: () -> Unit,
    onToggleFeatured: () -> Unit,
    onToggleTrending: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = BrandOrange
                ) {
                    Text(
                        text = article.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Edit button
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Story",
                            tint = BrandOrangeLight,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "By ${article.authorName} • ${article.viewCount} views",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badges Toggle Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = article.isBreaking,
                    onClick = onToggleBreaking,
                    label = { Text("Breaking", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BreakingRed,
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextMuted
                    )
                )

                FilterChip(
                    selected = article.isFeatured,
                    onClick = onToggleFeatured,
                    label = { Text("Hero", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandOrange,
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextMuted
                    )
                )

                FilterChip(
                    selected = article.isTrending,
                    onClick = onToggleTrending,
                    label = { Text("Trending", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandOrangeLight,
                        selectedLabelColor = Color.Black,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextMuted
                    )
                )
            }
        }
    }
}

@Composable
private fun CreateOrEditArticleView(
    existingArticle: Article?,
    categories: List<NewsCategory>,
    authors: List<Author>,
    onPublish: (
        id: Long,
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
        isTrending: Boolean
    ) -> Unit,
    onCancelEdit: () -> Unit
) {
    var title by remember(existingArticle) { mutableStateOf(existingArticle?.title ?: "") }
    var shortDesc by remember(existingArticle) { mutableStateOf(existingArticle?.shortDescription ?: "") }
    var content by remember(existingArticle) { mutableStateOf(existingArticle?.content ?: "") }
    var selectedCat by remember(existingArticle, categories) {
        mutableStateOf(existingArticle?.category ?: categories.firstOrNull()?.name ?: "Bihar")
    }
    var imageUrl by remember(existingArticle) {
        mutableStateOf(existingArticle?.imageUrl ?: "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=1200&q=80")
    }
    var imageCaption by remember(existingArticle) {
        mutableStateOf(existingArticle?.imageCaption ?: "CJPTV Newsroom Field Report")
    }
    var isBreaking by remember(existingArticle) { mutableStateOf(existingArticle?.isBreaking ?: false) }
    var isFeatured by remember(existingArticle) { mutableStateOf(existingArticle?.isFeatured ?: false) }
    var isTrending by remember(existingArticle) { mutableStateOf(existingArticle?.isTrending ?: false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val author = authors.firstOrNull() ?: Author(
        id = 1,
        name = "Sambhav Gupta",
        slug = "sambhav-gupta",
        role = "Founder & Editor-in-Chief",
        bio = "Founder and Editor-in-Chief of CJPTV.in"
    )

    val imagePresets = listOf(
        "Patna Secretariat" to "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=1200&q=80",
        "Parliament / Delhi" to "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?auto=format&fit=crop&w=1200&q=80",
        "Tech & Space" to "https://images.unsplash.com/photo-1517976487508-e7b8979ca6f3?auto=format&fit=crop&w=1200&q=80",
        "Economy & Markets" to "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=1200&q=80",
        "Sports / Cricket" to "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80",
        "Breaking Banner" to "https://images.unsplash.com/photo-1504711434969-e33886168f5c?auto=format&fit=crop&w=1200&q=80"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (existingArticle != null) "Edit Story #${existingArticle.id}" else "Upload News Story to CJPTV.in",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Quick Auto-Fill Demo Story Button
                if (existingArticle == null) {
                    TextButton(
                        onClick = {
                            title = "Bihar Cabinet Clears ₹12,500 Cr Greenfield Industrial Expressway Across 6 Districts"
                            shortDesc = "Major infrastructure push approved in cabinet meeting chaired by the Chief Minister, aiming to connect North and South Bihar hubs."
                            content = "PATNA — In a landmark policy decision, the Bihar state cabinet today formally cleared the ₹12,500 Crore Greenfield Industrial Expressway project connecting six key industrial and agricultural districts.\n\nAccording to official spokespersons, the new 4-lane access-controlled highway will significantly reduce freight transit time between Patna, Muzaffarpur, Darbhanga, and Bhagalpur. Industrialists and agricultural cooperatives have welcomed the milestone decision, anticipating boost in cold storage supply chains and local manufacturing corridors."
                            selectedCat = "Bihar"
                            imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=1200&q=80"
                            imageCaption = "File photo: Bihar State Secretariat, Patna"
                            isBreaking = true
                            isFeatured = true
                            validationError = null
                        }
                    ) {
                        Icon(imageVector = Icons.Default.AutoFixHigh, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Auto-Fill Sample", fontSize = 12.sp, color = BrandOrange)
                    }
                }
            }
        }

        // Error message banner if validation fails
        if (validationError != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BreakingRed.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BreakingRed)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = BreakingRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = validationError ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (it.isNotBlank()) validationError = null
                },
                label = { Text("Article Headline *") },
                placeholder = { Text("e.g. Bihar Cabinet approves landmark solar policy") },
                singleLine = true,
                isError = validationError != null && title.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedLabelColor = BrandOrange,
                    unfocusedLabelColor = TextMuted
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_article_title_input")
            )
        }

        item {
            OutlinedTextField(
                value = shortDesc,
                onValueChange = { shortDesc = it },
                label = { Text("Lead Summary / Standfirst *") },
                placeholder = { Text("Brief 1-2 sentence overview of the news story...") },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedLabelColor = BrandOrange,
                    unfocusedLabelColor = TextMuted
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = content,
                onValueChange = {
                    content = it
                    if (it.isNotBlank()) validationError = null
                },
                label = { Text("Full Article Body (Markdown supported) *") },
                placeholder = { Text("Write or paste the full journalism report here...") },
                minLines = 7,
                isError = validationError != null && content.isBlank(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedLabelColor = BrandOrange,
                    unfocusedLabelColor = TextMuted
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_article_body_input")
            )
        }

        item {
            Text("Select News Beat / Category:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCat.equals(cat.name, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCat = cat.name },
                        label = { Text(cat.name, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandOrange,
                            selectedLabelColor = Color.White,
                            containerColor = DarkSurfaceVariant,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Lead Image URL") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedLabelColor = BrandOrange,
                    unfocusedLabelColor = TextMuted
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Quick Presets:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                imagePresets.forEach { (presetName, url) ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (imageUrl == url) BrandOrangeContainer else DarkSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (imageUrl == url) BrandOrange else DarkCardBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { imageUrl = url }
                    ) {
                        Text(
                            text = presetName,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = if (imageUrl == url) BrandOrangeLight else TextSecondary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = imageCaption,
                onValueChange = { imageCaption = it },
                label = { Text("Image Caption / Photo Credit") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedLabelColor = BrandOrange,
                    unfocusedLabelColor = TextMuted
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("Editorial Placement Flags:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = isBreaking,
                    onClick = { isBreaking = !isBreaking },
                    label = { Text("Mark Breaking", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BreakingRed,
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextMuted
                    )
                )
                FilterChip(
                    selected = isFeatured,
                    onClick = { isFeatured = !isFeatured },
                    label = { Text("Hero Card", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandOrange,
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextMuted
                    )
                )
                FilterChip(
                    selected = isTrending,
                    onClick = { isTrending = !isTrending },
                    label = { Text("Trending", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandOrangeLight,
                        selectedLabelColor = Color.Black,
                        containerColor = DarkSurfaceVariant,
                        labelColor = TextMuted
                    )
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (existingArticle != null) {
                    OutlinedButton(
                        onClick = onCancelEdit,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }
                }

                Button(
                    onClick = {
                        if (title.isBlank()) {
                            validationError = "Please enter an article headline."
                            return@Button
                        }
                        if (content.isBlank()) {
                            validationError = "Please enter article content."
                            return@Button
                        }

                        val catSlug = categories.find { it.name.equals(selectedCat, ignoreCase = true) }?.slug ?: "bihar"
                        onPublish(
                            existingArticle?.id ?: 0L,
                            title,
                            shortDesc.ifBlank { title },
                            content,
                            selectedCat,
                            catSlug,
                            author.id,
                            author.name,
                            author.role,
                            imageUrl,
                            imageCaption,
                            isBreaking,
                            isFeatured,
                            isTrending
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("admin_publish_article_btn")
                ) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (existingArticle != null) "Update Story" else "Publish to CJPTV.in",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriesManagerView(
    categories: List<NewsCategory>,
    onAddCategory: (String, String, String, String) -> Unit,
    onDeleteCategory: (NewsCategory) -> Unit
) {
    val context = LocalContext.current
    var newCatName by remember { mutableStateOf("") }
    var newCatDesc by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Add New News Category / Beat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newCatName,
                        onValueChange = { newCatName = it },
                        label = { Text("Category Name (e.g. Cinema, Defense, Crime)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandOrange,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = newCatDesc,
                        onValueChange = { newCatDesc = it },
                        label = { Text("Description") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandOrange,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (newCatName.isNotBlank()) {
                                onAddCategory(newCatName, newCatName.lowercase().replace(" ", "-"), "#FF6321", newCatDesc)
                                Toast.makeText(context, "Category '$newCatName' added!", Toast.LENGTH_SHORT).show()
                                newCatName = ""
                                newCatDesc = ""
                            } else {
                                Toast.makeText(context, "Please enter a category name", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Add Category", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = "EXISTING CATEGORIES (${categories.size})",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontWeight = FontWeight.Bold
            )
        }

        items(categories) { cat ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = cat.name, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Slug: /${cat.slug}", fontSize = 11.sp, color = TextMuted)
                    }

                    IconButton(onClick = { onDeleteCategory(cat) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsManagerView(
    settings: AdminSettings,
    onResetDemoData: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Newsroom System Diagnostics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Portal: ${settings.siteName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Tagline: ${settings.tagline}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Contact: ${settings.contactEmail}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Text(
                    text = "Security: Encrypted PIN Protected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LiveGreen
                )
            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Database Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reset newsroom to the comprehensive Bihar & National journalism seed dataset.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        onResetDemoData()
                        Toast.makeText(context, "Demo journalism dataset restored!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandOrange),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Re-populate Comprehensive Seed Stories")
                }
            }
        }
    }
}
