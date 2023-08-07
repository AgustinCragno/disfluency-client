package com.disfluency.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DIALOG_BUILD_TIME = 100L

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AnimatedDialog(
    animationTime: Long = 200,
    dismissAction: () -> Unit,
    content: @Composable (() -> Unit) -> Unit
){
    val coroutineScope = rememberCoroutineScope()
    val animateVisibility = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        launch {
            delay(DIALOG_BUILD_TIME)
            animateVisibility.value = true
        }
    }

    val onDismiss = {
        coroutineScope.launch {
            animateVisibility.value = false
            delay(animationTime)
            dismissAction()
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = animateVisibility.value,
            enter = scaleIn(tween(animationTime.toInt())),
            exit = scaleOut(tween(animationTime.toInt()))
        ) {
            content { onDismiss() }
        }
    }
}