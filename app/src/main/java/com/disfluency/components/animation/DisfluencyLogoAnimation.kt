package com.disfluency.components.animation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.viewmodel.LoggedUserViewModel
import com.disfluency.viewmodel.LoginState
import kotlinx.coroutines.delay

private const val ANIMATION_LENGTH = 800L
private const val ANIMATION_CYCLE_COUNT = 4

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
    var atEnd by remember { mutableStateOf(viewModel.firstLoadDone.value) }

    DisfluencyLogo(atEnd)

    LaunchedEffect(Unit){
        if (!viewModel.firstLoadDone.value){

            var i = 0
            while (i <= ANIMATION_CYCLE_COUNT || viewModel.loginState == LoginState.SUBMITTED){
                delay(ANIMATION_LENGTH)
                atEnd = !atEnd
                i++
            }

            viewModel.firstLoadDone.value = true
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun DisfluencyLogo(atEnd: Boolean = true){
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.disfluency_logo_animation)

    Image(
        painter = rememberAnimatedVectorPainter(animatedImageVector = image, atEnd = atEnd),
        contentDescription = stringResource(R.string.app_name),
        modifier = Modifier.size(170.dp, 170.dp)
    )
}