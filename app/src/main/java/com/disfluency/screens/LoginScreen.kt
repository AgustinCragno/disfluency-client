package com.disfluency.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.disfluency.components.animation.DisfluencyAnimatedLogoRise

@Composable
fun LoginScreen(){
    val animationState = remember { mutableStateOf(false) }

    DisfluencyAnimatedLogoRise(animationState = animationState, riseOffset = 120.dp)
}