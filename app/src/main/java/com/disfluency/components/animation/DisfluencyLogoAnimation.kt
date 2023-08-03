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
import kotlinx.coroutines.delay

private const val ANIMATION_LENGTH = 800L
private const val ANIMATION_CYCLE_COUNT = 4

@Composable
fun DisfluencyAnimatedLogoRise(animationState: MutableState<Boolean>, riseOffset: Dp){
    val offset = animateDpAsState(targetValue = if (animationState.value) riseOffset else 0.dp,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(
        Modifier
            .fillMaxSize()
            .offset(y = -offset.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        DisfluencyAnimatedLogo(animationState)
    }
}

@Composable
fun DisfluencyAnimatedLogo(animationState: MutableState<Boolean>){
    var atEnd by remember { mutableStateOf(animationState.value) }

    DisfluencyLogo(atEnd)

    LaunchedEffect(Unit){
        if (!animationState.value){
            for (i in 0..ANIMATION_CYCLE_COUNT){
                delay(ANIMATION_LENGTH)
                atEnd = !atEnd
            }

            animationState.value = true
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