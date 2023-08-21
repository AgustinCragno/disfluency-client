package com.disfluency.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class RecordScreenViewModel() : ViewModel() {

    val isMenuExtended = mutableStateOf(false)

    val audioAmplitudes: MutableList<Float> = randomWaves(35) as MutableList<Float>

    private fun randomWaves(n: Int): List<Float> {
        return (1..n).map { Random.nextFloat() }
    }
}