package com.disfluency.screens.patient.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.components.bar.HomeTopAppBar
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.screens.patient.home.carousel.LastTwoWeeksRecapPanel
import com.disfluency.screens.patient.home.carousel.PatientWelcomePanel
import com.disfluency.screens.patient.home.carousel.PendingAssignmentsPanel
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.viewmodel.LoggedUserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.absoluteValue

@Preview
@Composable
private fun HomePatientPreview(){

    val patient = Patient(
        id = "",
        name = "",
        lastName = "",
        dateOfBirth = LocalDate.now(),
        email = "",
        joinedSince = LocalDate.now(),
        avatarIndex = 1,
        weeklyTurn = listOf(DayOfWeek.MONDAY),
        weeklyHour = LocalTime.now()
    )

    val navController = rememberNavController()

    val viewModel = LoggedUserViewModel(navController)

    DisfluencyTheme() {
        HomePatientScreen(patient = patient, navController = navController, viewModel = viewModel)
    }
}

@Composable
fun HomePatientScreen(patient: Patient, navController: NavHostController, viewModel: LoggedUserViewModel){
    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Patient.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HomeTopAppBar(navController = navController, viewModel = viewModel)

            HomeScreenContent()
        }
    }
}

@Composable
private fun HomeScreenContent(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WelcomeBackCarousel()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun WelcomeBackCarousel(){

    val pagerState = rememberPagerState()
    val autoScrollDuration = 5000L

    val items = listOf<@Composable () -> Unit>(
        { PatientWelcomePanel() },
        { PendingAssignmentsPanel() },
        { LastTwoWeeksRecapPanel() }
    )

    val pageCount = items.size

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            var currentPageKey by remember { mutableStateOf(0) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }

    Box {
        HorizontalPager(count = pageCount, state = pagerState) { page ->
            items[page].invoke()
        }

        DotIndicators(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            pageCount = pageCount,
            pagerState = pagerState
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun DotIndicators(
    pageCount: Int,
    pagerState: PagerState,
    selectedColor: Color = Color.White.copy(alpha = 0.5f),
    unselectedColor: Color = Color.DarkGray.copy(alpha = 0.5f),
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) selectedColor else unselectedColor
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffset).absoluteValue

        val transformation =
            lerp(
                start = 0.7f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        alpha = transformation
        scaleY = transformation
    }

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start * (1 - fraction) + stop * fraction
}
