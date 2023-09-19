package com.disfluency.utilities.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

@Composable
fun Color.mix(color: Color, percentage: Float = 0.5f): Color {
    return Color(ColorUtils.blendARGB(this.toArgb(), color.toArgb(), percentage))
}