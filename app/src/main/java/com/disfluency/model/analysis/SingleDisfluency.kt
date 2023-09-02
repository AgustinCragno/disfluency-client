package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color

data class SingleDisfluency(val disfluencyType: DisfluencyType): Disfluency {
    override fun getDisfluency(): String {
        return disfluencyType.toString()
    }

    override fun getColor(): Color {
        return disfluencyType.color
    }
}