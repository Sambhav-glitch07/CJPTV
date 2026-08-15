package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale

class NewsRepository(private val db: AppDatabase) {
    private val articleDao = db.articleDao()
    private val categoryDao = db.categoryDao()
    private val authorDao = db.authorDao()
    private val liveUpdateDao = db.liveUpdateDao()
    private val videoNewsDao = db.videoNewsDao()
    private val mediaItemDao = db.mediaItemDao()
    private val settingsDao = db.settingsDao()

    // Articles
    val allArticles: Flow<List<Article>> = articleDao.getAllArticles()
    val publishedArticles: Flow<List<Article>> = articleDao.getPublishedArticles()
    val draftArticles: Flow<List<Article>> = articleDao.getDraftArticles()
    val breakingArticles: Flow<List<Article>> = articleDao.getBreakingArticles()
    val featuredArticles: Flow<List<Article>> = articleDao.getFeaturedArticles()
    val trendingArticles: Flow<List<Article>> = articleDao.getTrendingArticles()

    fun getArticlesByCategory(categorySlug: String): Flow<List<Article>> =
        articleDao.getArticlesByCategory(categorySlug)

    suspend fun getArticleBySlug(slug: String): Article? =
        articleDao.getArticleBySlug(slug)

    suspend fun getArticleById(id: Long): Article? =
        articleDao.getArticleById(id)

    fun searchArticles(query: String): Flow<List<Article>> =
        articleDao.searchArticles(query)

    fun getRelatedArticles(categorySlug: String, currentId: Long): Flow<List<Article>> =
        articleDao.getRelatedArticles(categorySlug, currentId)

    suspend fun incrementViewCount(id: Long) =
        articleDao.incrementViewCount(id)

    suspend fun saveArticle(article: Article): Long =
        articleDao.insertArticle(article)

    suspend fun updateArticle(article: Article) =
        articleDao.updateArticle(article)

    suspend fun deleteArticle(article: Article) =
        articleDao.deleteArticle(article)

    suspend fun deleteArticleById(id: Long) =
        articleDao.deleteById(id)

    // Categories
    val allCategories: Flow<List<NewsCategory>> = categoryDao.getAllCategories()
    suspend fun getCategoryBySlug(slug: String): NewsCategory? = categoryDao.getCategoryBySlug(slug)
    suspend fun saveCategory(category: NewsCategory): Long = categoryDao.insertCategory(category)
    suspend fun updateCategory(category: NewsCategory) = categoryDao.updateCategory(category)
    suspend fun deleteCategory(category: NewsCategory) = categoryDao.deleteCategory(category)
    suspend fun deleteCategoryById(id: Long) = categoryDao.deleteById(id)

    // Authors
    val allAuthors: Flow<List<Author>> = authorDao.getAllAuthors()
    suspend fun getAuthorBySlug(slug: String): Author? = authorDao.getAuthorBySlug(slug)
    suspend fun getAuthorById(id: Long): Author? = authorDao.getAuthorById(id)
    suspend fun saveAuthor(author: Author): Long = authorDao.insertAuthor(author)
    suspend fun updateAuthor(author: Author) = authorDao.updateAuthor(author)
    suspend fun deleteAuthor(author: Author) = authorDao.deleteAuthor(author)
    suspend fun deleteAuthorById(id: Long) = authorDao.deleteById(id)

    // Live Updates
    val allLiveUpdates: Flow<List<LiveUpdate>> = liveUpdateDao.getAllLiveUpdates()
    suspend fun saveLiveUpdate(update: LiveUpdate): Long = liveUpdateDao.insertLiveUpdate(update)
    suspend fun deleteLiveUpdate(update: LiveUpdate) = liveUpdateDao.deleteLiveUpdate(update)
    suspend fun deleteLiveUpdateById(id: Long) = liveUpdateDao.deleteById(id)

    // Video News
    val allVideos: Flow<List<VideoNews>> = videoNewsDao.getAllVideos()
    suspend fun saveVideo(video: VideoNews): Long = videoNewsDao.insertVideo(video)
    suspend fun deleteVideo(video: VideoNews) = videoNewsDao.deleteVideo(video)
    suspend fun deleteVideoById(id: Long) = videoNewsDao.deleteById(id)

    // Media
    val allMedia: Flow<List<MediaItem>> = mediaItemDao.getAllMedia()
    fun searchMedia(query: String): Flow<List<MediaItem>> = mediaItemDao.searchMedia(query)
    suspend fun saveMedia(media: MediaItem): Long = mediaItemDao.insertMedia(media)
    suspend fun deleteMedia(media: MediaItem) = mediaItemDao.deleteMedia(media)
    suspend fun deleteMediaById(id: Long) = mediaItemDao.deleteById(id)

    // Settings
    val settingsFlow: Flow<AdminSettings?> = settingsDao.getSettingsFlow()
    suspend fun getSettings(): AdminSettings = settingsDao.getSettings() ?: AdminSettings()
    suspend fun saveSettings(settings: AdminSettings) = settingsDao.saveSettings(settings)

    suspend fun ensureInitialized() {
        val existingSettings = settingsDao.getSettings()
        if (existingSettings == null) {
            seedInitialData()
        }
    }

    suspend fun resetToSeedData() {
        articleDao.clearAll()
        categoryDao.clearAll()
        authorDao.clearAll()
        liveUpdateDao.clearAll()
        videoNewsDao.clearAll()
        mediaItemDao.clearAll()
        seedInitialData()
    }

    suspend fun clearAllNewsData() {
        articleDao.clearAll()
        liveUpdateDao.clearAll()
        videoNewsDao.clearAll()
        mediaItemDao.clearAll()
    }

    private suspend fun seedInitialData() {
        // Settings
        settingsDao.saveSettings(
            AdminSettings(
                id = 1,
                siteName = "CJPTV.in",
                tagline = "Your News. Your Voice.",
                contactEmail = "editorial@cjptv.in",
                breakingTickerEnabled = true,
                defaultSeoTitle = "CJPTV.in - Latest India, Bihar & World News | Your News. Your Voice.",
                defaultSeoDesc = "CJPTV.in delivers fast, credible, and comprehensive news from Bihar, India, and around the world, founded by Sambhav Gupta.",
                adminPin = "061289",
                founderName = "Sambhav Gupta",
                founderTitle = "Founder & Editor-in-Chief",
                founderBio = "Sambhav Gupta is the founder of CJPTV.in, dedicated to delivering honest, fearless, and grassroots digital journalism for India.",
                isDemoLoaded = true
            )
        )

        // Categories
        val defaultCategories = listOf(
            NewsCategory(name = "India", slug = "india", description = "National headlines, policy, and breaking stories across India", colorHex = "#FF6B00", sortOrder = 1),
            NewsCategory(name = "Bihar", slug = "bihar", description = "Comprehensive grassroots and regional reporting from Patna and all 38 districts of Bihar", colorHex = "#E63946", sortOrder = 2),
            NewsCategory(name = "World", slug = "world", description = "Global affairs, diplomacy, geopolitics, and international relations", colorHex = "#3A86FF", sortOrder = 3),
            NewsCategory(name = "Politics", slug = "politics", description = "Parliamentary updates, elections, state assemblies, and policy insights", colorHex = "#8338EC", sortOrder = 4),
            NewsCategory(name = "Business", slug = "business", description = "Markets, economy, startups, corporate developments, and finance", colorHex = "#2A9D8F", sortOrder = 5),
            NewsCategory(name = "Sports", slug = "sports", description = "Cricket, athletics, football, and international sporting tournament coverage", colorHex = "#E76F51", sortOrder = 6),
            NewsCategory(name = "Entertainment", slug = "entertainment", description = "Cinema, OTT releases, art, music, and cultural events", colorHex = "#F72585", sortOrder = 7),
            NewsCategory(name = "Technology", slug = "technology", description = "AI breakthroughs, semiconductors, consumer gadgets, and digital tech", colorHex = "#4CC9F0", sortOrder = 8),
            NewsCategory(name = "Science", slug = "science", description = "Space exploration, ISRO missions, discoveries, and environmental research", colorHex = "#06D6A0", sortOrder = 9),
            NewsCategory(name = "Education", slug = "education", description = "Academic reforms, exams, competitive tests, and university news", colorHex = "#FFB703", sortOrder = 10),
            NewsCategory(name = "Health", slug = "health", description = "Public health initiatives, medical breakthroughs, and wellness guidelines", colorHex = "#118AB2", sortOrder = 11)
        )
        categoryDao.insertAll(defaultCategories)

        // Authors
        val defaultAuthors = listOf(
            Author(
                id = 1,
                name = "Sambhav Gupta",
                slug = "sambhav-gupta",
                role = "Founder & Editor-in-Chief",
                bio = "Founder of CJPTV.in. Committed to independent, courageous, and public-interest digital journalism across Bihar and India.",
                email = "sambhav@cjptv.in",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                twitter = "@sambhavgupta",
                linkedin = "sambhav-gupta"
            ),
            Author(
                id = 2,
                name = "Priya Sharma",
                slug = "priya-sharma",
                role = "Senior Political Editor",
                bio = "Specializing in parliamentary affairs, electoral analysis, and national governance policies.",
                email = "priya.sharma@cjptv.in",
                avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
                twitter = "@priyapolity",
                linkedin = "priyasharma-news"
            ),
            Author(
                id = 3,
                name = "R.K. Verma",
                slug = "rk-verma",
                role = "Chief Bihar Bureau Chief",
                bio = "Over 15 years reporting on Bihar's administrative developments, grassroots infrastructure, and cultural heritage.",
                email = "rk.verma@cjptv.in",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                twitter = "@rkvermapatna",
                linkedin = "rkverma-bihar"
            ),
            Author(
                id = 4,
                name = "Amit Sen",
                slug = "amit-sen",
                role = "Tech & Science Editor",
                bio = "Covering artificial intelligence, frontier space missions, and Indian deep-tech innovations.",
                email = "amit.sen@cjptv.in",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
                twitter = "@amitsentech",
                linkedin = "amitsen-tech"
            )
        )
        authorDao.insertAll(defaultAuthors)

        val now = System.currentTimeMillis()
        val hour = 3600_000L

        // Articles
        val defaultArticles = listOf(
            Article(
                id = 1,
                slug = "patna-metro-phase-2-tunneling-breakthrough-underground-junction",
                title = "Patna Metro Phase 2 Tunneling Breakthrough: Direct Underground Connectivity to Patna Junction by Year-End",
                shortDescription = "Tunnel boring machines completed the pivotal 1.8-kilometer underground stretch connecting Rajendra Nagar to Patna Junction, paving the way for expedited commercial trials.",
                content = """
# Breakthrough in Bihar's Flagship Urban Transit Project

In a milestone development for Bihar's urban infrastructure, the **Patna Metro Rail Corporation (PMRC)** on Friday announced the successful breakthrough of its flagship Tunnel Boring Machine (TBM) 'Mahavir' at the underground Patna Junction metro station box.

The underground tunnel spans **1.84 kilometers** from Rajendra Nagar Terminal to Patna Junction, navigating through complex geological strata beneath high-density urban residential zones with zero surface disruptions.

> "This breakthrough marks the culmination of 14 months of 24x7 precision tunneling. We are on track to inaugurate the priority underground corridor ahead of schedule," stated the Chief Project Director.

### Key Highlights of the Expansion
* **Dual Parallel Tunnels:** High-capacity twin tunnels with modern safety cross-passages every 250 meters.
* **Smart Signaling:** Advanced Communication-Based Train Control (CBTC) enabling 3-minute train frequency during peak hours.
* **Green Transit:** Projected to reduce carbon emissions by over 120,000 metric tons annually across the Patna metropolitan area.
* **Intermodal Hub:** Direct seamless pedestrian subways connecting the Indian Railways concourse with the metro platform.

Commercial trial runs on the underground segment are scheduled to commence in the final quarter of this year, revolutionizing daily commuter transit for over 800,000 residents.
                """.trimIndent(),
                category = "Bihar",
                categorySlug = "bihar",
                authorId = 1,
                authorName = "Sambhav Gupta",
                authorRole = "Founder & Editor-in-Chief",
                imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Subway tunneling works in progress along the Patna underground corridor.",
                isBreaking = true,
                isFeatured = true,
                isTrending = true,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (1 * hour),
                updatedAt = now - (30 * 60 * 1000L),
                viewCount = 3840,
                readTimeMinutes = 4
            ),
            Article(
                id = 2,
                slug = "india-semiconductor-fabrication-facility-dholera-tech-leap",
                title = "India's Mega Semiconductor Fab Facility Begins Cleanroom Equipment Installation in Dholera",
                shortDescription = "The landmark facility, built in collaboration with global chipmakers, is set to produce commercial 28nm and 40nm microcontrollers powering electric mobility and telecom gear.",
                content = """
# India Steps Firmly onto the Global Silicon Map

The ambitious Indian Semiconductor Mission (ISM) reached a historic milestone today as precision cleanroom manufacturing tools arrived at the flagship **Dholera Semiconductor Mega-Fab**.

The multi-billion dollar facility will produce indigenous 28nm and 40nm logic chips designed for automotive safety microcontrollers, smart energy grids, and next-generation 5G base stations.

### Strategic Impact
1. **Supply Chain Resilience:** Eliminates critical component dependencies for India's domestic electronics manufacturing sector.
2. **High-Skilled Employment:** Generating over 20,000 direct high-tech engineering and cleanroom operational positions.
3. **Ecosystem Catalyst:** 45 ancillary chemical, gas, and wafer packaging partners are concurrently setting up operations adjacent to the fab park.

> "India is transitioning from consumer of global hardware to an essential node in global high-precision technology manufacturing," noted the Union IT Minister during the press conference.
                """.trimIndent(),
                category = "Technology",
                categorySlug = "technology",
                authorId = 4,
                authorName = "Amit Sen",
                authorRole = "Tech & Science Editor",
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Silicon wafer inspection in a state-of-the-art semiconductor fabrication cleanroom.",
                isBreaking = true,
                isFeatured = false,
                isTrending = true,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (2 * hour),
                updatedAt = now - (1 * hour),
                viewCount = 2920,
                readTimeMinutes = 5
            ),
            Article(
                id = 3,
                slug = "parliament-monsoon-session-landmark-digital-governance-bill",
                title = "Parliament Monsoon Session: Landmark Digital Governance & Citizen Data Protection Framework Table",
                shortDescription = "New legislation introduces streamlined digital dispute resolution, algorithmic transparency mandates for public services, and fortified privacy protections for rural internet users.",
                content = """
# Transformative Legislation in the Lok Sabha

The Monsoon Session of Parliament witnessed lively debates today as the Treasury benches tabled the **Comprehensive Digital Governance & Citizen Empowerment Bill, 2026**.

The legislation addresses three pivotal dimensions of modern governance:
* **Algorithmic Accountability:** Government automated systems impacting welfare distribution must undergo mandatory bias and accessibility audits.
* **Decentralized Grievance Portals:** Enabling citizens in tier-2 and rural regions to register complaints via voice-enabled regional language chatbots.
* **Data Sovereignty Protection:** Stringent criminal penalties for unauthorized commercial telemetry and data exfiltration.

Opposition leaders welcomed the core provisions while demanding a Joint Parliamentary Committee review on specific compliance timelines for small tech enterprises.
                """.trimIndent(),
                category = "Politics",
                categorySlug = "politics",
                authorId = 2,
                authorName = "Priya Sharma",
                authorRole = "Senior Political Editor",
                imageUrl = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Parliament House illuminated during the Monsoon legislative session.",
                isBreaking = false,
                isFeatured = false,
                isTrending = true,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (4 * hour),
                updatedAt = now - (2 * hour),
                viewCount = 2150,
                readTimeMinutes = 4
            ),
            Article(
                id = 4,
                slug = "bihar-organic-farming-corridor-ganga-districts-export-hub",
                title = "Bihar's Organic Farming Corridor Across 12 Ganga Districts Records 40% Export Growth",
                shortDescription = "Empowering over 150,000 smallholders with organic certification, cold storage clusters, and direct international cargo flights out of Patna Airport.",
                content = """
# Green Revolution 2.0 in the Gangetic Plains

Bihar's dedicated **Organic Farming Corridor (Jaivik Corridor)** spanning 12 districts flanking the holy river Ganga—including Nalanda, Buxar, Vaishali, and Bhagalpur—has achieved unprecedented economic yields.

According to agricultural ministry statistics released today, organic vegetable and grain exports from the state registered a **40.8% year-on-year surge**, reaching premium markets across the Middle East, Southeast Asia, and European Union.

### Factors Fueling Success
* **Direct Cargo Flights:** Dedicated temperature-controlled cargo handling at Jay Prakash Narayan Airport in Patna.
* **Zero-Chemical Certification:** Subsidized government soil testing and blockchain-backed provenance tracking.
* **Farmer Producer Organizations (FPOs):** Empowering rural women self-help collectives to eliminate exploitative intermediary margins.

Farmers in Nalanda reported average household income increases of 65% since shifting from chemical-intensive crops to organic aromatic rice and vegetables.
                """.trimIndent(),
                category = "Bihar",
                categorySlug = "bihar",
                authorId = 3,
                authorName = "R.K. Verma",
                authorRole = "Chief Bihar Bureau Chief",
                imageUrl = "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Lush organic paddy fields along the Ganga river basin in Bihar.",
                isBreaking = false,
                isFeatured = false,
                isTrending = false,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (6 * hour),
                updatedAt = now - (6 * hour),
                viewCount = 1780,
                readTimeMinutes = 3
            ),
            Article(
                id = 5,
                slug = "isro-advanced-hypersonic-reusable-launch-vehicle-landing-experiment",
                title = "ISRO Successfully Executes Autonomous Hypersonic Reusable Launch Vehicle Touchdown",
                shortDescription = "The indigenous space agency accomplished a pinpoint autonomous landing of its winged aerospace test vehicle at the Aeronautical Test Range in Challakere.",
                content = """
# India Masters Reusable Aerospace Technology

The Indian Space Research Organisation (**ISRO**) on Wednesday achieved another historic triumph with the flawless autonomous runway landing of the **Reusable Launch Vehicle - Autonomous Landing Experiment (RLV-LEX-3)**.

Released from an Indian Air Force Chinook helicopter at an altitude of 4.5 kilometers, the winged spacecraft autonomously adjusted for gusty wind shears, aligned its glide slope, and executed a center-line wheel touchdown at 350 km/h with parachute deceleration.

> "This test proves our indigenous guidance algorithms, high-temperature composite thermal tiles, and advanced retractable landing gear are flight-ready for orbital missions," ISRO announced.

The success brings India significantly closer to reducing satellite launch expenditures by upwards of **70%**, ensuring affordable commercial access to outer space.
                """.trimIndent(),
                category = "Science",
                categorySlug = "science",
                authorId = 4,
                authorName = "Amit Sen",
                authorRole = "Tech & Science Editor",
                imageUrl = "https://images.unsplash.com/photo-1517976487502-d5e0c5210c4f?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "ISRO space launch rocket on the launchpad at Sriharikota.",
                isBreaking = false,
                isFeatured = false,
                isTrending = true,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (8 * hour),
                updatedAt = now - (8 * hour),
                viewCount = 4120,
                readTimeMinutes = 4
            ),
            Article(
                id = 6,
                slug = "t20-asian-championship-thriller-final-over-heroics",
                title = "T20 Asian Championship: Sensational Final-Over Victory Sparks Nationwide Celebrations",
                shortDescription = "A breathtaking last-ball boundary sealed an exhilarating 4-wicket triumph in a high-octane final clash, igniting joyful street festivities in Patna, Delhi, and Mumbai.",
                content = """
# Unforgettable Night of Cricket Drama

In one of the most pulsating finals in recent cricketing history, Team India snatched victory from the jaws of defeat to lift the prestigious **T20 Asian Championship Trophy**.

Needing 17 runs in the final over against world-class pace bowling, the young middle-order duo held their nerve:
* Ball 1: Hard length delivery, 2 runs taken
* Ball 2: Towering straight six over long-on
* Ball 3: Dot ball under yorker length
* Ball 4: 2 runs through deep mid-wicket
* Ball 5: Single taken
* Ball 6: **Magnificent upper-cut over third man for FOUR!**

Fireworks illuminated skylines across Patna's Gandhi Maidan, Mumbai's Marine Drive, and Bengaluru's MG Road as fans celebrated into the early morning hours.
                """.trimIndent(),
                category = "Sports",
                categorySlug = "sports",
                authorId = 1,
                authorName = "Sambhav Gupta",
                authorRole = "Founder & Editor-in-Chief",
                imageUrl = "https://images.unsplash.com/photo-1531415074968-036ba1b575da?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Celebrations after the championship-winning final stroke.",
                isBreaking = false,
                isFeatured = false,
                isTrending = true,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (10 * hour),
                updatedAt = now - (9 * hour),
                viewCount = 5230,
                readTimeMinutes = 3
            ),
            Article(
                id = 7,
                slug = "ai-powered-rural-medical-diagnosis-centers-bihar",
                title = "AI-Powered Tele-Diagnostic Centers Roll Out Across 150 Rural Health Blocks in Bihar",
                shortDescription = "Equipped with automated ECG analysis, digital retinopathy scanners, and instant specialist consultations, the initiative drastically cuts down healthcare travel times.",
                content = """
# Healthcare Equity Through Technology

In a pioneering digital health initiative, the Bihar Health Department has inaugurated **150 AI-Integrated Tele-Diagnostic Centers** across underserved rural community health outposts.

The centers feature automated digital diagnostic suites capable of:
* Instant ECG and cardiac rhythm anomaly detection within 90 seconds.
* AI retinal screening for diabetic retinopathy and early glaucoma.
* Rapid cloud transmission to super-specialty doctors at AIIMS Patna for instant prescription generation.

Patients in remote villages who previously traveled over 80 kilometers for basic cardiac workups can now receive validated diagnoses within minutes free of cost.
                """.trimIndent(),
                category = "Health",
                categorySlug = "health",
                authorId = 3,
                authorName = "R.K. Verma",
                authorRole = "Chief Bihar Bureau Chief",
                imageUrl = "https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Healthcare professional reviewing digital medical diagnostics.",
                isBreaking = false,
                isFeatured = false,
                isTrending = false,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (14 * hour),
                updatedAt = now - (14 * hour),
                viewCount = 1430,
                readTimeMinutes = 4
            ),
            Article(
                id = 8,
                slug = "global-clean-energy-summit-south-asian-solar-grid-accord",
                title = "International Energy Accord: India Champions Cross-Border South Asian Green Solar Grid",
                shortDescription = "Multilateral partnership pledges unified renewable transmission corridors connecting solar and hydro reserves across India, Nepal, Bhutan, and Sri Lanka.",
                content = """
# Regional Energy Security & Decarbonization

At the International Climate Summit in New Delhi, delegates representing seven South Asian nations ratified the historic **One Sun, One Grid Regional Energy Integration Accord**.

Under the framework, high-voltage direct current (HVDC) transmission lines will interconnect Himalayan hydroelectric surplus with the extensive solar mega-parks of Western and Southern India.

> "Clean electrons know no borders. This grid sharing allows surplus daytime solar energy in Rajasthan to power night operations across the subcontinent," emphasized the Energy Secretary.
                """.trimIndent(),
                category = "World",
                categorySlug = "world",
                authorId = 2,
                authorName = "Priya Sharma",
                authorRole = "Senior Political Editor",
                imageUrl = "https://images.unsplash.com/photo-1509391365360-2e959784a276?auto=format&fit=crop&w=1200&q=80",
                imageCaption = "Expansive solar panel arrays producing clean renewable electricity.",
                isBreaking = false,
                isFeatured = false,
                isTrending = false,
                isLive = false,
                status = "PUBLISHED",
                publishedAt = now - (18 * hour),
                updatedAt = now - (18 * hour),
                viewCount = 1890,
                readTimeMinutes = 4
            )
        )
        articleDao.insertAll(defaultArticles)

        // Live Updates
        val defaultLiveUpdates = listOf(
            LiveUpdate(
                id = 1,
                topicTitle = "Bihar Legislative Assembly Monsoon Budget Session",
                timeLabel = "11:45 AM IST",
                headline = "Finance Minister presents ₹3.15 Lakh Crore Annual Budget",
                content = "Key outlays allocated for flood mitigation, youth entrepreneurship seed funds, and industrial corridor development along the Purvanchal Expressway.",
                badge = "OFFICIAL",
                timestamp = now - (15 * 60 * 1000L)
            ),
            LiveUpdate(
                id = 2,
                topicTitle = "Bihar Legislative Assembly Monsoon Budget Session",
                timeLabel = "11:15 AM IST",
                headline = "Special debate underway on Ganga Riverfront Tourism Project",
                content = "Legislators from Patna and Vaishali highlight the tourist footfall potential of the eco-riverfront and inland waterway passenger cruise terminals.",
                badge = "DEVELOPMENT",
                timestamp = now - (45 * 60 * 1000L)
            ),
            LiveUpdate(
                id = 3,
                topicTitle = "Bihar Legislative Assembly Monsoon Budget Session",
                timeLabel = "10:30 AM IST",
                headline = "Speaker commences Question Hour; Agriculture MSP raised",
                content = "Members question the status of procurement centers and solar irrigation pumps distribution in northern flood-prone districts.",
                badge = "UPDATE",
                timestamp = now - (90 * 60 * 1000L)
            ),
            LiveUpdate(
                id = 4,
                topicTitle = "National Weather Alert: Heavy Monsoonal Rainfall Across Gangetic Plains",
                timeLabel = "09:00 AM IST",
                headline = "IMD issues Orange Alert for 14 districts in Eastern India",
                content = "State disaster management authority advises riverine communities to stay alert as water levels in Gandak and Kosi maintain steady vigilance.",
                badge = "URGENT",
                timestamp = now - (150 * 60 * 1000L)
            )
        )
        liveUpdateDao.insertAll(defaultLiveUpdates)

        // Video News
        val defaultVideos = listOf(
            VideoNews(
                id = 1,
                title = "Inside Patna's High-Tech Underground Metro Tunnels: Special Ground Report",
                description = "CJPTV Exclusive: Founder Sambhav Gupta walks through the newly excavated Rajendra Nagar to Patna Junction underground metro rail tunnel.",
                youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=800&q=80",
                category = "Bihar",
                duration = "08:24",
                publishedAt = now - (3 * hour),
                views = 14200
            ),
            VideoNews(
                id = 2,
                title = "How India's Dholera Semiconductor Plant Will Change Global Tech Dynamics",
                description = "An in-depth analysis of India's Silicon mission, cleanroom engineering, and export targets with Tech Desk Editor Amit Sen.",
                youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80",
                category = "Technology",
                duration = "12:15",
                publishedAt = now - (6 * hour),
                views = 9850
            ),
            VideoNews(
                id = 3,
                title = "Ganga Organic Corridor: How Bihar Farmers Are Transforming Agritech",
                description = "Ground report from Nalanda and Buxar on organic certifications and direct export cargo chains.",
                youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailUrl = "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=800&q=80",
                category = "Business",
                duration = "06:50",
                publishedAt = now - (12 * hour),
                views = 7600
            ),
            VideoNews(
                id = 4,
                title = "Championship Finals Highlights: The Winning Over Breakdown",
                description = "Tactical ball-by-ball analysis of the sensational final over victory that claimed the Asian trophy.",
                youtubeUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                thumbnailUrl = "https://images.unsplash.com/photo-1531415074968-036ba1b575da?auto=format&fit=crop&w=800&q=80",
                category = "Sports",
                duration = "05:10",
                publishedAt = now - (16 * hour),
                views = 28400
            )
        )
        videoNewsDao.insertAll(defaultVideos)

        // Media items
        val defaultMedia = listOf(
            MediaItem(
                id = 1,
                title = "Patna Metro Underground Tunnel Construction",
                url = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?auto=format&fit=crop&w=1200&q=80",
                altText = "Patna metro construction tunnel",
                dimensions = "1200x800"
            ),
            MediaItem(
                id = 2,
                title = "Semiconductor Fabrication Microchip Wafer",
                url = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=1200&q=80",
                altText = "Semiconductor wafer",
                dimensions = "1200x800"
            ),
            MediaItem(
                id = 3,
                title = "Parliament House Legislative Assembly",
                url = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?auto=format&fit=crop&w=1200&q=80",
                altText = "Parliament House",
                dimensions = "1200x800"
            ),
            MediaItem(
                id = 4,
                title = "Organic Agricultural Farm Fields",
                url = "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?auto=format&fit=crop&w=1200&q=80",
                altText = "Organic paddy fields",
                dimensions = "1200x800"
            ),
            MediaItem(
                id = 5,
                title = "Spacecraft Aerospace Rocket Launch",
                url = "https://images.unsplash.com/photo-1517976487502-d5e0c5210c4f?auto=format&fit=crop&w=1200&q=80",
                altText = "ISRO Rocket launchpad",
                dimensions = "1200x800"
            ),
            MediaItem(
                id = 6,
                title = "Stadium Cricket Sports Arena",
                url = "https://images.unsplash.com/photo-1531415074968-036ba1b575da?auto=format&fit=crop&w=1200&q=80",
                altText = "Cricket stadium crowd",
                dimensions = "1200x800"
            )
        )
        mediaItemDao.insertAll(defaultMedia)
    }

    companion object {
        fun slugify(text: String): String {
            return text.lowercase(Locale.ROOT)
                .replace(Regex("[^a-z0-9\\s-]"), "")
                .trim()
                .replace(Regex("\\s+"), "-")
                .take(80)
        }
    }
}
