package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

data class CompositeDisfluency(val multipleDisfluency: List<DisfluencyType>): Disfluency {
    override fun getDisfluency(): String {
        return multipleDisfluency.joinToString(prefix = "[", separator = "+", postfix = "]")
    }

    override fun getColor(): Color {
        return multipleDisfluency.map(DisfluencyType::color).reduce(Color::compositeOver)
    }
}
