package com.disfluency.components.animation

import androidx.compose.animation.Animatable
import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.disfluency.R
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.states.LoginState
import kotlinx.coroutines.delay

private const val ANIMATION_LENGTH = 300L
private const val ANIMATION_CYCLE_COUNT = 10

@Composable
fun DisfluencyAnimatedLogoRise(viewModel: LoggedUserViewModel, riseOffset: Dp){
    val riseAnimationState = remember(viewModel.loginState, viewModel.firstLoadDone.value) {
        mutableStateOf(viewModel.loginState < LoginState.AUTHENTICATED && viewModel.firstLoadDone.value)
    }

    val offset = animateDpAsState(targetValue = if (riseAnimationState.value) riseOffset else 0.dp,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(
        Modifier
            .fillMaxSize()
            .offset(y = -offset.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DisfluencyAnimatedLogo(viewModel)
    }
}

@Composable
private fun DisfluencyAnimatedLogo(viewModel: LoggedUserViewModel){
    val animate = !viewModel.firstLoadDone.value
    var points by remember { mutableStateOf(if (animate) 0 else 3) }

    DisfluencyLogo(points)

    LaunchedEffect(Unit){
        if (animate){

            var i = 0
            while (i <= ANIMATION_CYCLE_COUNT || viewModel.loginState == LoginState.SUBMITTED){
                delay(ANIMATION_LENGTH)
                points = (points + 1) % 4
                i++
            }

            viewModel.firstLoadDone.value = true
        }
    }
}

@Composable
fun DisfluencyLogo(pointCount: Int = 3){
    Image(
        painter = painterResource(id = R.drawable.disfluency_logo_empty),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier
            .size(170.dp, 170.dp)
            .drawWithContent {
                drawContent()

                val startX = size.width / 2.7f
                val spacing = 43f
                val y = size.height / 2.57f

                val points = (0 until pointCount).map { Offset(startX + (spacing * it), y) }

                drawPoints(
                    points = points,
                    pointMode = PointMode.Points,
                    brush = SolidColor(Color(255, 100, 0)),
                    strokeWidth = 22f,
                    cap = StrokeCap.Round
                )
            }
    )
}

@Preview
@Composable
private fun DisfluencyAnimatedLogoPreview(){

    val navHostController = rememberNavController()
    val viewModel = LoggedUserViewModel(navHostController)

    DisfluencyTheme() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DisfluencyAnimatedLogo(viewModel)
        }
    }
}