package com.disfluency.components.inputs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.utilities.format.formatDayOfWeek
import java.time.DayOfWeek

@Composable
fun WeeklyTurnPicker(selectedDays: SnapshotStateList<DayOfWeek>){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DayOfWeek.values().take(5).forEach { day ->
            TextButton(
                modifier = Modifier
                    .size(50.dp)
                    .padding(4.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDays.contains(day)) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (selectedDays.contains(day)) Color.White else Color.DarkGray
                ),
                onClick = {
                    if (selectedDays.contains(day)) selectedDays.remove(day)
                    else selectedDays.add(day)
                }
            ) {
                Text(
                    text = formatDayOfWeek(dayOfWeek = day)[0].toString(),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}