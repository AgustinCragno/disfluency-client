package com.disfluency.utilities.format

import androidx.compose.runtime.MutableState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun formatLocalTimeState(state: MutableState<LocalTime?>): String{
    return state.value?.let { formatLocalTime(it) } ?: ""
}

fun formatLocalTime(time: LocalTime): String{
    return time.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
}