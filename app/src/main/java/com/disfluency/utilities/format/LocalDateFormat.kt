package com.disfluency.utilities.format

import androidx.compose.runtime.MutableState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatLocalDateState(state: MutableState<LocalDate?>): String{
    return state.value?.let { formatLocalDate(it) } ?: ""
}

fun formatLocalDate(localDate: LocalDate): String{
    return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun formatMillisecondsAsLocalDate(milliseconds: Long): LocalDate {
    return Instant.ofEpochMilli(milliseconds)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}