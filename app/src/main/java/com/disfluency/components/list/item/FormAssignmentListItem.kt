package com.disfluency.components.list.item

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.form.FormAssignment
import com.disfluency.utilities.format.formatLocalDate

@Composable
fun FormAssignmentListItem(
    formAssignment: FormAssignment,
    onClick: () -> Unit
){
    ListItem(
        title = formAssignment.form.title,
        subtitle = formatLocalDate(formAssignment.date),
        trailingContent = {
            val color = if (formAssignment.completionEntries.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray

            IconLabeled(
                icon = Icons.Default.Repeat,
                label = formAssignment.completionEntries.count().toString(),
                iconColor = color,
                labelColor = color,
                labelSize = 15.sp
            )
        },
        onClick = onClick
    )
}

@Composable
fun NoFormAssignmentsMessage(){
    ImageMessagePage(
        imageResource = R.drawable.form_fill,
        text = stringResource(id = R.string.patient_has_no_assigned_forms)
    )
}