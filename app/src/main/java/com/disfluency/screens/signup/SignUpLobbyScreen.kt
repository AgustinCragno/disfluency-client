package com.disfluency.screens.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.random.randomSign

@Composable
fun SignUpLobbyScreen(navController: NavHostController) {
    val exitAnimationState = remember { mutableStateOf(false) }

    BackNavigationScaffold(
        title = R.string.signup,
        navController = navController,
        onBackNavigation = { navController.navigate(Route.Launch.path) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.signup_message),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                UserRoleBanner(
                    title = stringResource(R.string.as_patient),
                    subtitle = stringResource(R.string.patient_role_description),
                    image = R.drawable.patient_banner,
                    modifier = Modifier.weight(0.5f),
                    exitState = exitAnimationState,
                    action = {
                        exitAnimationState.value = true
                        navController.navigate(Route.SignUpPatient.path)
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                UserRoleBanner(
                    title = stringResource(R.string.as_therapist),
                    subtitle = stringResource(R.string.therapist_role_description),
                    image = R.drawable.therapist_banner,
                    modifier = Modifier.weight(0.5f),
                    exitState = exitAnimationState,
                    action = {
                        exitAnimationState.value = true
                        navController.navigate(Route.SignUpTherapist.path)
                    }
                )

            }
        }
    }
}



@Composable
private fun UserRoleBanner(
    title: String,
    subtitle: String,
    image: Int,
    modifier: Modifier = Modifier,
    exitState: MutableState<Boolean>,
    action: () -> Unit
) {
    val sign = randomSign()

    AnimatedVisibility(
        visible = !exitState.value,
        exit = slideOutHorizontally(targetOffsetX = { 1000 * sign }),
        modifier = modifier
    ) {
        Box(modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { action() }
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = image),
                contentDescription = "User Role Thumbnail",
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0F to Color.Transparent,
                            .5F to Color.Black.copy(alpha = 0.5F),
                            1F to Color.Black.copy(alpha = 0.8F)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LobbyPreview() {
    DisfluencyTheme {
        SignUpLobbyScreen(navController = rememberNavController())
    }
}
