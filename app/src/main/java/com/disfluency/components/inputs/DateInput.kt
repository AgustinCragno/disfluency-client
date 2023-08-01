package com.disfluency.components.inputs

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.disfluency.R
import com.disfluency.utilities.format.formatLocalDateState
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

@Composable
fun DateInput(state: MutableState<LocalDate?>, label: String){
    var formattedValue by rememberSaveable { mutableStateOf(formatLocalDateState(state)) }

    var wrongValue: Boolean by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val todaysYear = calendar.get(Calendar.YEAR)
    val todaysMonth = calendar.get(Calendar.MONTH) + 1
    val todaysDay = calendar.get(Calendar.DAY_OF_MONTH)

    val currentYear = state.value?.year ?: todaysYear
    val currentMonth = state.value?.monthValue ?: todaysMonth
    val currentDay = state.value?.dayOfMonth ?: todaysDay

    val onDateSetListener: (DatePicker, Int, Int, Int) -> Unit = { _, year, month, day ->
        state.value = LocalDate.of(year, month + 1, day)
        formattedValue = formatLocalDateState(state)
    }

    val datePickerDialog = DatePickerDialog(LocalContext.current, onDateSetListener, currentYear, currentMonth, currentDay)
    val dateTomorrow = LocalDate.of(todaysYear, todaysMonth, todaysDay).plusDays(1).atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()!!
    datePickerDialog.datePicker.maxDate = dateTomorrow

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
            .clickable { datePickerDialog.show() })
    }
}