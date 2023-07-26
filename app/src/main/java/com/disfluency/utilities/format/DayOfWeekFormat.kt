package com.disfluency.utilities.format

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.disfluency.R
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun formatDayOfWeek(dayOfWeek: DayOfWeek): String{
    return dayOfWeek.getDisplayName(TextStyle.FULL, Locale(stringResource(R.string.locale))).replaceFirstChar { it.uppercase() }
}