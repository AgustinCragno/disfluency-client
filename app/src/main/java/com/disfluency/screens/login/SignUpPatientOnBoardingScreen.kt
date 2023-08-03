package com.disfluency.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.navigation.routing.Route
import com.disfluency.ui.theme.DisfluencyTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SignUpPatientOnBoardingScreen(navController: NavHostController){

    val pages = listOf(
        OnBoardingPage.Introduction, OnBoardingPage.Vision, OnBoardingPage.Registration, OnBoardingPage.Invite, OnBoardingPage.Recommend
    )

    val pagerState = rememberPagerState()

    val coroutineScope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            modifier = Modifier.weight(10f),
            count = pages.size,
            state = pagerState
        ) { position ->
            OnBoardingPageScreen(page = pages[position])
        }

        HorizontalPagerIndicator(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterHorizontally),
            pagerState = pagerState
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.skip))
            }

            TextButton(
                onClick = {
                    coroutineScope.launch {
                        nextPage(pagerState, navController, pages.size)
                    }
                }
            ) {
                Text(
                    text = stringResource(
                        if (pagerState.currentPage < pages.size - 1) R.string.next
                        else R.string.understood
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private suspend fun nextPage(pagerState: PagerState, navController: NavHostController, pagesCount: Int){
    if (pagerState.currentPage >= pagesCount - 1){
        navController.navigate(Route.SignUpLobby.path)
    }else{
        pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }
}

private open class OnBoardingPage(val title: Int, val headline: Int, val image: Int, val trailingContent: Int){
    object Introduction: OnBoardingPage(title = R.string.welcome, headline = R.string.onboarding_introduction_1, image = R.drawable.speech_in_screen, trailingContent = R.string.onboarding_introduction_2)
    object Vision: OnBoardingPage(title = R.string.our_vision, headline = R.string.onboarding_vision_1, image = R.drawable.avatar_3, trailingContent = R.string.onboarding_vision_2)
    object Registration: OnBoardingPage(title = R.string.register, headline = R.string.onboarding_registration_1, image = R.drawable.form_fill, trailingContent = R.string.onboarding_registration_2)
    object Invite: OnBoardingPage(title = R.string.invite, headline = R.string.onboarding_invite_1, image = R.drawable.invitation, trailingContent = R.string.onboarding_invite_2)
    object Recommend: OnBoardingPage(title = R.string.and_done, headline = R.string.onboarding_recommend_1, image = R.drawable.avatar_1, trailingContent = R.string.onboarding_recommend_2)
}

@Composable
private fun OnBoardingPageScreen(page: OnBoardingPage){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = stringResource(id = page.title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp),
            text = stringResource(id = page.headline),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Image(
            modifier = Modifier
                .size(200.dp)
                .padding(32.dp),
            painter = painterResource(id = page.image),
            contentDescription = "",
            alpha = 0.6f
        )

        Text(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp),
            text = stringResource(id = page.trailingContent),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun OnBoardingPreview(){

    DisfluencyTheme() {
        SignUpPatientOnBoardingScreen(navController = rememberNavController())
    }
}