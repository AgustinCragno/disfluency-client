package com.disfluency.components.list.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.model.user.Patient
import com.disfluency.utilities.format.formatWeeklyTurn
import java.time.format.DateTimeFormatter

@Composable
fun PatientListItem(patient: Patient, onClick: () -> Unit = {}, leadingContentPrefix: @Composable () -> Unit = {}) {
    ListItem(
        title = patient.fullNameFormal(),
        subtitle = formatWeeklyTurn(patient.weeklyTurn),
        subtitleColor = MaterialTheme.colorScheme.primary,
        leadingContent = {
            Row {
                leadingContentPrefix()
                Image(
                    painter = painterResource(patient.avatar()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                )
            }
        },
        trailingContent = {
            IconLabeled(
                icon = Icons.Outlined.AccessTime,
                label = patient.weeklyHour.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.time_format)
                    ))
            )
        },
        onClick = onClick
    )
}