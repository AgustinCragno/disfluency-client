package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver


enum class DisfluencyType(val color: Color) {
    V(Color.Magenta),
    I(Color(0.016f, 0.592f, 0.667f, 1.0f)),
    M(Color(1.0f, 0.757f, 0.035f, 1.0f)),
    RF(Color(0.298f, 0.686f, 0.314f, 1.0f)),
    RP(Color.Blue),
    RS(Color.Red),
    RSI(Color.Gray)
}
