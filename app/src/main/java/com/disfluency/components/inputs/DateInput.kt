package com.disfluency.components.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.disfluency.R
import com.disfluency.utilities.format.formatLocalDateState
import com.disfluency.utilities.format.formatMillisecondsAsLocalDate
import java.time.LocalDate
import java.time.ZoneOffset

//TODO: Investigar si hay forma de cambiar el formato de las fechas en el DatePicker. Esta como mm/dd/aaaa.

//TODO: arreglar dia que se elige disminuye en uno.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(state: MutableState<LocalDate?>, label: String){
    var formattedValue by rememberSaveable { mutableStateOf(formatLocalDateState(state)) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    var openDialog by remember { mutableStateOf(false) }

    val disableDialog = { openDialog = false }

    var wrongValue: Boolean by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = formattedValue,
            onValueChange = {
                wrongValue = !MandatoryValidation().validate(formattedValue)
            },
            label = { Text(label) },
            singleLine = true,
            isError = wrongValue,
            trailingIcon = {
                if (wrongValue) Icon(Icons.Filled.Info, "Error")
                else Icon(Icons.Filled.CalendarToday, "Fecha", tint = MaterialTheme.colorScheme.primary)
            },
            supportingText = {
                if(wrongValue) Text(text = stringResource(id = R.string.required_field))
            }
        )

        Box(modifier = Modifier
            .matchParentSize()
            .clickable { openDialog = true })
    }

    val maxDateAsMilliseconds: Long? = LocalDate.now().atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    if (openDialog) {
        DatePickerDialog(
            onDismissRequest = disableDialog,
            confirmButton = {
                TextButton(
                    onClick = {
                        state.value = formatMillisecondsAsLocalDate(datePickerState.selectedDateMillis!!)
                        formattedValue = formatLocalDateState(state)
                        disableDialog()
                    },
                    enabled = datePickerState.selectedDateMillis!=null
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(disableDialog) {
                    Text(stringResource(R.string.go_back))
                }
            },
            content = {
                DatePicker(state = datePickerState, title = null, headline = null, showModeToggle = true,
                    dateValidator = {
                        maxDateAsMilliseconds!=null && it < maxDateAsMilliseconds
                    }
                )
            }
        )
    }
}