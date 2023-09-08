package com.disfluency.utilities.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun Color.darken(percentage: Float): Color {
    val resultARGB = ColorUtils.blendARGB(this.toArgb(), android.graphics.Color.BLACK, percentage)
    return Color(resultARGB)
}