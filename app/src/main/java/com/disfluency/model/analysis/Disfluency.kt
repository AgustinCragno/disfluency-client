package com.disfluency.model.analysis

import androidx.compose.ui.graphics.Color

interface Disfluency {

    fun getDisfluency(): String

    fun getColor(): Color
}