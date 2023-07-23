package com.disfluency.utilities.format

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.disfluency.R
import java.time.DayOfWeek

@Composable
fun formatWeeklyTurn(weeklyTurn: List<DayOfWeek>): String{
    val days = weeklyTurn
        .map { d -> formatDayOfWeek(dayOfWeek = d) }
        .map { d -> d[0].uppercaseChar() + d.substring(1) }
    return if(weeklyTurn.size>1){
        val lastDay = days.last()
        val daysBeforeLast = days.dropLast(1)
        "${daysBeforeLast.joinToString(", ")} ${stringResource(id = R.string.and)} $lastDay"
    }
    else days.joinToString { it }
}