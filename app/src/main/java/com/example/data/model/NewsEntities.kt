package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val slug: String,
    val title: String,
    val shortDescription: String,
    val content: String,
    val category: String,
    val categorySlug: String,
    val authorId: Long = 1,
    val authorName: String = "Sambhav Gupta",
    val authorRole: String = "Founder & Editor-in-Chief",
    val imageUrl: String,
    val imageCaption: String = "",
    val isBreaking: Boolean = false,
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val isLive: Boolean = false,
    val status: String = "PUBLISHED", // "PUBLISHED", "DRAFT", "SCHEDULED"
    val publishedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val scheduledAt: Long? = null,
    val viewCount: Int = 120,
    val readTimeMinutes: Int = 4,
    val videoUrl: String? = null
)

@Entity(tableName = "categories")
data class NewsCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val slug: String,
    val description: String = "",
    val colorHex: String = "#FF6B00",
    val sortOrder: Int = 0
)

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val slug: String,
    val role: String,
    val bio: String,
    val email: String = "",
    val avatarUrl: String = "",
    val twitter: String = "",
    val linkedin: String = ""
)

@Entity(tableName = "live_updates")
data class LiveUpdate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val articleId: Long? = null,
    val topicTitle: String,
    val timeLabel: String,
    val headline: String,
    val content: String,
    val badge: String = "UPDATE", // "URGENT", "OFFICIAL", "DEVELOPMENT", "UPDATE"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "video_news")
data class VideoNews(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val youtubeUrl: String,
    val thumbnailUrl: String,
    val category: String,
    val duration: String = "03:45",
    val publishedAt: Long = System.currentTimeMillis(),
    val views: Int = 540
)

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val url: String,
    val mediaType: String = "IMAGE",
    val altText: String = "",
    val uploadedAt: Long = System.currentTimeMillis(),
    val dimensions: String = "1200x800"
)

@Entity(tableName = "admin_settings")
data class AdminSettings(
    @PrimaryKey
    val id: Int = 1,
    val siteName: String = "CJPTV.in",
    val tagline: String = "Your News. Your Voice.",
    val contactEmail: String = "editorial@cjptv.in",
    val breakingTickerEnabled: Boolean = true,
    val defaultSeoTitle: String = "CJPTV.in - Latest India, Bihar & World News | Your News. Your Voice.",
    val defaultSeoDesc: String = "CJPTV.in delivers fast, credible, and comprehensive news from Bihar, India, and around the world, founded by Sambhav Gupta.",
    val adminPin: String = "061289",
    val founderName: String = "Sambhav Gupta",
    val founderTitle: String = "Founder & Editor-in-Chief",
    val founderBio: String = "Sambhav Gupta is the founder of CJPTV.in, dedicated to delivering honest, fearless, and grassroots digital journalism for India.",
    val isDemoLoaded: Boolean = true
)
