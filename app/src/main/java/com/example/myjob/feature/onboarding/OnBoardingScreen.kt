package com.example.myjob.feature.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myjob.R
import com.example.myjob.feature.navigation.Screen
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────
//  COLORS
// ─────────────────────────────────────────────
private val Green     = Color(0xFF049344)
private val GreenDark = Color(0xFF037A38)
private val GreenBg   = Color(0xFFE8F5EE)
private val GreenMid  = Color(0xFFD1EAD9)
private val White     = Color.White
private val Ink       = Color(0xFF111827)
private val InkMed    = Color(0xFF374151)
private val Muted     = Color(0xFF6B7280)
private val SubMuted  = Color(0xFF9CA3AF)
private val BgLight   = Color(0xFFF8FFFE)
private val BgGray    = Color(0xFFF3F4F6)
private val Border    = Color(0xFFE5E7EB)
private val BlueBg    = Color(0xFFEFF6FF)
private val BlueText  = Color(0xFF2563EB)

// ─────────────────────────────────────────────
//  PAGE MODEL
// ─────────────────────────────────────────────
data class OnboardingPage(
    val stepLabel: String,
    val title: String,
    val accentWord: String,
    val description: String,
    val stat1Number: String,
    val stat1Label: String,
    val stat2Number: String,
    val stat2Label: String,
    val illustration: @Composable () -> Unit
)

// ─────────────────────────────────────────────
//  PAGES DEFINITION
// ─────────────────────────────────────────────
@Composable
private fun buildPages(): List<OnboardingPage> = listOf(

    // ── 1. Candidate Profile ──────────────────
    OnboardingPage(
        stepLabel    = stringResource(R.string.step1),
        title        = stringResource(R.string.step1_title),
        accentWord   = "Professional",
        description  = "Build a complete profile with your skills, portfolio, and experience. Let employers find you — and match to the right roles automatically.",
        stat1Number  = "12K+",
        stat1Label   = "Artist profiles",
        stat2Number  = "94%",
        stat2Label   = "Match rate",
        illustration = { CandidateIllustration() }
    ),

    // ── 2. Company / Employer ─────────────────
    OnboardingPage(
        stepLabel    = "Step 2 of 3  ·  For Companies",
        title        = "Post Jobs & Find Talent",
        accentWord   = "Find Talent",
        description  = "Publish job listings, review verified artist profiles, and connect directly with the right candidate — all on one platform.",
        stat1Number  = "850+",
        stat1Label   = "Companies hiring",
        stat2Number  = "2,400",
        stat2Label   = "Live job posts",
        illustration = { CompanyIllustration() }
    ),

    // ── 3. Service / Freelance ────────────────
    OnboardingPage(
        stepLabel    = "Step 3 of 3  ·  Services",
        title        = "List Services, Earn More",
        accentWord   = "Earn More",
        description  = "Publish your freelance services with a clear price. Clients find and book you directly — no middleman, no delays, secure payment.",
        stat1Number  = "3,200",
        stat1Label   = "Services listed",
        stat2Number  = "48h",
        stat2Label   = "Avg. first booking",
        illustration = { ServiceIllustration() }
    )

)

// ─────────────────────────────────────────────
//  MAIN SCREEN
// ─────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
) {
    val pages      = buildPages()
    val pagerState = rememberPagerState { pages.size }
    val scope      = rememberCoroutineScope()
    val current    = pagerState.currentPage
    val isLast     = current == pages.lastIndex

    val screenAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        screenAlpha.animateTo(1f, tween(350))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .graphicsLayer {
                alpha = screenAlpha.value
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ── TOP BAR ───────────────────────
            TopBar(current, pages.size, isLast, {
                onBoardingViewModel.finishOnBoarding()
                val token = onBoardingViewModel.getToken()

                val destination =
                    if (token.isNotEmpty()) Screen.HomeScreen.route
                    else Screen.LoginScreen.route

                navController.navigate(destination)
            })

            // ── PAGER ─────────────────────────
            HorizontalPager(
                state    = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { idx ->
                PageContent(page = pages[idx])
            }

            // ── BOTTOM NAV ────────────────────
            BottomNav(
                current = current,
                total   = pages.size,
                isLast  = isLast,
                onBack  = { scope.launch { pagerState.animateScrollToPage(current - 1) } },
                onNext  = {
                    if (isLast) {
                        onBoardingViewModel.finishOnBoarding()
                        val token = onBoardingViewModel.getToken()

                        val destination =
                            if (token.isNotEmpty()) Screen.HomeScreen.route
                            else Screen.LoginScreen.route

                        navController.navigate(destination)
                    }
                    else scope.launch { pagerState.animateScrollToPage(current + 1) }
                }
            )
        }
    }
}

// ─────────────────────────────────────────────
//  TOP BAR — logo + progress + skip
// ─────────────────────────────────────────────
@Composable
private fun TopBar(
    current: Int, total: Int, isLast: Boolean, onSkip: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.newlogo),
                contentDescription = "logo",
                modifier = Modifier.size(30.dp)
            )

            // Skip
            if (!isLast) {
                Text(
                    text = "Skip",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SubMuted,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSkip
                    )
                )
            }
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Border)
        ) {
            val progress by animateFloatAsState(
                targetValue = (current + 1f) / total,
                animationSpec = tween(400),
                label = "progress"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Brush.horizontalGradient(listOf(Green, Color(0xFF25D366))))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─────────────────────────────────────────────
//  PAGE CONTENT
// ─────────────────────────────────────────────
@Composable
private fun PageContent(page: OnboardingPage) {
    Column(modifier = Modifier.fillMaxSize()) {

        // ── ILLUSTRATION BLOCK ────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.50f)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(BgLight)
                .border(1.dp, Border, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            page.illustration()
        }

        Spacer(Modifier.height(20.dp))

        // ── TEXT BLOCK ────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.50f)
                .padding(horizontal = 24.dp)
        ) {
            // Step label
            Text(
                text = page.stepLabel,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = SubMuted,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(10.dp))

            // Title with accent word
            Text(
                text = buildAnnotatedString {
                    val parts = page.title.split(page.accentWord)
                    if (parts.size >= 2) {
                        append(parts[0])
                        withStyle(SpanStyle(color = Green)) { append(page.accentWord) }
                        append(parts[1])
                    } else {
                        append(page.title)
                    }
                },
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 30.sp,
                    color = Ink
                )
            )

            Spacer(Modifier.height(12.dp))

            // Description
            Text(
                text = page.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Muted,
                lineHeight = 20.sp
            )
        }
    }
}

// ─────────────────────────────────────────────
//  STAT BOX
// ─────────────────────────────────────────────
@Composable
private fun StatBox(number: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgGray)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column {
            Text(number, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Green)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = SubMuted)
        }
    }
}

// ─────────────────────────────────────────────
//  BOTTOM NAVIGATION
// ─────────────────────────────────────────────
@Composable
private fun BottomNav(
    current: Int,
    total: Int,
    isLast: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 22.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back
        Box(modifier = Modifier.size(42.dp)) {
            if (current > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(BgGray)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onBack
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", fontSize = 18.sp, color = InkMed, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Dots
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
            repeat(total) { i ->
                val active = i == current
                val w by animateDpAsState(
                    targetValue = if (active) 22.dp else 6.dp,
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
                    label = "dot_$i"
                )
                val a by animateFloatAsState(
                    targetValue = if (active) 1f else 0.22f,
                    animationSpec = tween(250),
                    label = "alpha_$i"
                )
                Box(
                    modifier = Modifier
                        .width(w)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Green.copy(alpha = a))
                )
            }
        }

        // Next / Get Started
        Button(
            onClick = onNext,
            modifier = Modifier
                .height(42.dp)
                .then(if (isLast) Modifier.width(138.dp) else Modifier.size(42.dp)),
            shape = if (isLast) RoundedCornerShape(50.dp) else CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Green, contentColor = White),
            elevation = ButtonDefaults.buttonElevation(3.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            if (isLast) {
                Text("Get Started", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            } else {
                Text("→", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// ─────────────────────────────────────────────
//  ─── ILLUSTRATIONS ───────────────────────────
//  Professional UI mockups, no artistic drawing
// ─────────────────────────────────────────────

// ── 1. Candidate: Profile card mockup ────────
@Composable
private fun CandidateIllustration() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                // Green header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green)
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("SA", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = White)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sana Ayari", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                            Text("Visual Artist · Tunis", fontSize = 10.sp, color = White.copy(0.75f))
                        }
                        // Open to work chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(White.copy(0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Open to work", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = White)
                        }
                    }
                }
                // Body
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Skills
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Illustration", "Photography", "Branding").forEach { skill ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GreenBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(skill, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Green)
                            }
                        }
                    }
                    // Divider
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border))
                    // Stats row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            "5 yrs" to "Experience",
                            "42" to "Projects",
                            "4.9 ★" to "Rating"
                        ).forEach { (value, label) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    value,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (label == "Rating") Green else Ink
                                )
                                Text(label, fontSize = 9.sp, color = SubMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── 2. Company: Job cards list ────────────────
@Composable
private fun CompanyIllustration() {
    val companies = listOf(
        Triple("GF", "Galerie Farhat", "Gallery · Sfax"),
        Triple("SA", "Studio Atlas", "Design Studio · Tunis"),
        Triple("JF", "Jazz Festival Org.", "Events · Carthage")
    )
    val colors = listOf(Green, BlueText, Color(0xFFD97706))
    val bgColors = listOf(GreenBg, BlueBg, Color(0xFFFFF7ED))
    val textColors = listOf(Green, BlueText, Color(0xFFD97706))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            companies.forEachIndexed { i, (initials, name, type) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(bgColors[i]),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                initials,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textColors[i]
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Ink)
                            Text(type, fontSize = 10.sp, color = SubMuted)
                        }
                        /*Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GreenBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(roles, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Green)
                        }*/
                    }
                }
            }
        }
    }
}

// ── 3. Service: Pricing list ──────────────────
@Composable
private fun ServiceIllustration() {
    val services = listOf(
        Triple("Brand Identity Design", "Logo · Guidelines · Assets", "800 TND / project"),
        Triple("Event Photography", "Full day · Edited photos", "500 TND / day"),
        Triple("Social Media Content", "Monthly package · 12 posts", "300 TND / month")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Services", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text("+ Add New", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Green)
                }
                services.forEachIndexed { i, (name, sub, price) ->
                    if (i > 0) Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Border))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Ink)
                            Text(sub, fontSize = 10.sp, color = SubMuted)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                price.substringBefore(" /"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Green
                            )
                            Text(
                                "/ " + price.substringAfter("/ "),
                                fontSize = 9.sp,
                                color = SubMuted
                            )
                        }
                    }
                }
            }
        }
    }
}