package com.disfluency.components.list.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.Route
import com.disfluency.screens.therapist.patients.ActivityButton
import com.disfluency.utilities.format.formatWeeklyTurn
import java.time.format.DateTimeFormatter

@Composable
fun PatientListItem(
    patient: Patient,
    subtitle: String = formatWeeklyTurn(weeklyTurn = patient.weeklyTurn),
    onClick: () -> Unit = {},
    leadingContentPrefix: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
) {
    ListItem(
        title = patient.fullNameFormal(),
        subtitle = subtitle,
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
        trailingContent = trailingContent,
        onClick = onClick
    )
}

@Composable
fun PatientListItem(
    patient: Patient,
    onClick: () -> Unit = {},
    leadingContentPrefix: @Composable () -> Unit = {}
) {
    PatientListItem(
        patient = patient,
        onClick = onClick,
        leadingContentPrefix = leadingContentPrefix,
        trailingContent = {
            IconLabeled(
                icon = Icons.Outlined.AccessTime,
                label = patient.weeklyHour.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.time_format)
                    ))
            )
        }
    )
}

@Composable
fun PatientListItem(
    patient: Patient,
    onClick: () -> Unit = {},
    leadingContentPrefix: @Composable () -> Unit = {},
    navController: NavHostController? = null
) {
    PatientListItem(
        patient = patient,
        subtitle = patient.weeklyHour.format(
            DateTimeFormatter.ofPattern(
                stringResource(R.string.time_format)
            )
        ),
        onClick = onClick,
        leadingContentPrefix = leadingContentPrefix,
        trailingContent = {
            IconButton(
                modifier = Modifier.width(22.dp),
                onClick = {
                    navController?.navigate(Route.Therapist.PatientSessions.routeTo(patient.id))
                }
            ) {
                Icon(imageVector = Icons.Filled.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            }
        }
    )
}

@Composable
fun SelectablePatientListItem(
    patient: Patient,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    var selected by remember {
        mutableStateOf(false)
    }

    val colors = SelectableListItemColors.getColorsFromState(selected, enabled)

    ListItem(
        title = patient.fullNameFormal(),
        subtitle = formatWeeklyTurn(patient.weeklyTurn),
        subtitleColor = colors.label,
        leadingContent = {
            Row {
                val modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)

                if (selected || !enabled){
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = modifier,
                        tint = colors.icon
                    )
                } else {
                    Image(
                        painter = painterResource(patient.avatar()),
                        contentDescription = null,
                        modifier = modifier
                    )
                }
            }
        },
        trailingContent = {
            IconLabeled(
                icon = Icons.Outlined.AccessTime,
                label = patient.weeklyHour.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.time_format)
                    )),
                iconColor = colors.label
            )
        },
        colors = ListItemDefaults.colors(colors.background),
        onClick = {
            if (enabled) {
                selected = !selected
                onClick()
            }
        }
    )
}


class SelectableListItemColors(
    val background: Color,
    val icon: Color,
    val label: Color
){
    companion object {
        @Composable
        fun getColorsFromState(selected: Boolean, enabled: Boolean): SelectableListItemColors {
            if (!enabled){
                return SelectableListItemColors(
                    background = MaterialTheme.colorScheme.secondaryContainer,
                    icon = MaterialTheme.colorScheme.onSecondaryContainer,
                    label = MaterialTheme.colorScheme.secondary
                )
            }

            return if (selected){
                SelectableListItemColors(
                    background = MaterialTheme.colorScheme.onPrimaryContainer,
                    icon = MaterialTheme.colorScheme.primary,
                    label = MaterialTheme.colorScheme.primary
                )
            } else {
                SelectableListItemColors(
                    background = Color.Transparent,
                    icon = Color.Transparent,
                    label = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}