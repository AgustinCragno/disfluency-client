package com.disfluency.utilities.format

import androidx.compose.runtime.MutableState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

fun formatLocalDateState(state: MutableState<LocalDate?>): String{
    return state.value?.let { formatLocalDate(it) } ?: ""
}

fun formatLocalDate(localDate: LocalDate): String{
    return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

fun formatLocalDateAsWords(localDate: LocalDate, localeId: String): String{
    val locale = Locale(localeId)

    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
    val month = localDate.month.getDisplayName(TextStyle.FULL, locale)

    return "$dayOfWeek ${localDate.dayOfMonth} de $month, ${localDate.year}"
}

fun formatLocalDateAsMonthInWords(localDate: LocalDate, localeId: String): String{
    val locale = Locale(localeId)

    val month = localDate.month.getDisplayName(TextStyle.FULL, locale)

    return "${localDate.dayOfMonth} de $month, ${localDate.year}"
}