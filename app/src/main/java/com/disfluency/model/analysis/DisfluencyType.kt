package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color


enum class DisfluencyType(
    val fullName: String,
    val color: Color
) {
    V("vacilacion", Color.Magenta),
    I("interjeccion", Color(0.016f, 0.592f, 0.667f, 1.0f)),
    M("modificacion", Color(1.0f, 0.757f, 0.035f, 1.0f)),
    Rf("repeticion de frase", Color(0.298f, 0.686f, 0.314f, 1.0f)),
    Rp("repeticion de palabra", Color.Blue),
    Rs("repeticion de sonido", Color.Red),
    Rsi("repeticion de silaba", Color(0.933f, 0.459f, 0.31f, 1.0f))
}
