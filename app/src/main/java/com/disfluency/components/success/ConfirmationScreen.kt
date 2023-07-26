package com.disfluency.components.success

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.disfluency.screens.therapist.success.ON_SUCCESS_ANIMATION_TIME

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ConfirmationScreen(
    loadingState: MutableState<ConfirmationState>,
    loadingContent: @Composable () -> Unit,
    successContent: @Composable () -> Unit,
    errorContent: @Composable () -> Unit
){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = loadingState.value != ConfirmationState.DONE,
            enter = fadeIn(tween(durationMillis = ON_SUCCESS_ANIMATION_TIME * 4, delayMillis = ON_SUCCESS_ANIMATION_TIME * 2))
                    + scaleIn(tween(durationMillis = ON_SUCCESS_ANIMATION_TIME * 4, delayMillis = ON_SUCCESS_ANIMATION_TIME * 2)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME))
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary){}
        }

        AnimatedVisibility(
            visible = loadingState.value == ConfirmationState.LOADING,
            enter = fadeIn(tween(durationMillis = ON_SUCCESS_ANIMATION_TIME * 4, delayMillis = ON_SUCCESS_ANIMATION_TIME * 2)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME * 2)),
            content = { loadingContent() }
        )

        AnimatedVisibility(
            visible = loadingState.value == ConfirmationState.SUCCESS,
            enter = fadeIn(tween(durationMillis = 1, delayMillis = ON_SUCCESS_ANIMATION_TIME)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME)),
            content = { successContent() }
        )

        AnimatedVisibility(
            visible = loadingState.value == ConfirmationState.ERROR,
            enter = fadeIn(tween(durationMillis = 1, delayMillis = ON_SUCCESS_ANIMATION_TIME)),
            exit = fadeOut(tween(ON_SUCCESS_ANIMATION_TIME)),
            content = { errorContent() }
        )
    }
}

enum class ConfirmationState {
    LOADING, SUCCESS, ERROR, DONE
}