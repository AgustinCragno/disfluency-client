package com.disfluency.utilities.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Color.darken(percentage: Float = 0.5f): Color {
    return this.mix(color = Color.Black, percentage = percentage)
}

@Composable
fun Color.lighten(percentage: Float = 0.5f): Color {
    return this.mix(color = Color.White, percentage = percentage)
}