package com.disfluency.screens.therapist.home

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.disfluency.model.user.Therapist
import com.disfluency.screens.patient.home.DotIndicators
import com.disfluency.screens.therapist.home.carousel.PendingPatients
import com.disfluency.screens.therapist.home.carousel.TherapistDisfluencyPanel
import com.disfluency.screens.therapist.home.carousel.TherapistWelcomePanel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeBackCarousel(therapist: Therapist){

    val pagerState = rememberPagerState()
    val autoScrollDuration = 5000L

    val items = listOf<@Composable () -> Unit>(
        { TherapistWelcomePanel() },
        { TherapistDisfluencyPanel() },
        { PendingPatients(therapist) }
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