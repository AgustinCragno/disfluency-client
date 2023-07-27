package com.disfluency.utilities.color

import androidx.compose.ui.graphics.Color

fun stringToRGB(string: String): Color {
    val i = string.hashCode()

    val a = (i shr 24 and 0xFF)
    val r = (i shr 16 and 0xFF)
    val g = (i shr 8 and 0xFF)
    val b = (i and 0xFF)
    return Color(r, g, b, 255);
}