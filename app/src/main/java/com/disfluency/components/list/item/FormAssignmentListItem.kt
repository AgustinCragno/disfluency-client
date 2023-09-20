package com.disfluency.components.list.item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.disfluency.R
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
            Text(
                text = "${formAssignment.completionEntries.count()} " + if (formAssignment.completionEntries.count() != 1) stringResource(
                    R.string.resolutions)
                else stringResource(R.string.resolution)
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