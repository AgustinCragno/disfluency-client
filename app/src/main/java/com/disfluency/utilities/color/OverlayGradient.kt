package com.disfluency.utilities.color

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun verticalGradient(color: Color) = Brush.verticalGradient(
    0F to Color.Transparent,
    .25F to color.copy(alpha = 0.2F),
    .5F to color.copy(alpha = 0.5F),
    1F to color.copy(alpha = 0.8F)
)