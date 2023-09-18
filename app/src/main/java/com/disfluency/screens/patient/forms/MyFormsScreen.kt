package com.disfluency.screens.patient.forms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.model.form.FormAssignment
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.FormsViewModel

@Composable
fun MyFormsScreen(patient: Patient, navController: NavHostController, viewModel: FormsViewModel){
    LaunchedEffect(Unit) {
        viewModel.getAssignmentsOfPatient(patientId = patient.id)
    }

    val filterQuery = remember { mutableStateOf("") }

    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Patient.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(filterQuery = filterQuery)

            viewModel.assignedForms.value?.let { forms ->
                FormAssignmentList(
                    assignedForms = forms.filter { it.form.title.contains(filterQuery.value, true) },
                    navController = navController,
                    onClickRoute = Route.Patient.FormCompletion
                )
            }
        }
    }
}

// TODO hacer reutilizable?
@Composable
fun FormAssignmentList(assignedForms: List<FormAssignment>, navController: NavHostController, onClickRoute: Route){
    if (assignedForms.isEmpty()){
        NoAssignmentsMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignedForms) {ex ->
            FormAssignmentListItem(formAssignment = ex, navController = navController, onClickRoute = onClickRoute)
        }
    }
}

@Composable
fun FormAssignmentListItem(formAssignment: FormAssignment, navController: NavHostController, onClickRoute: Route){
    ListItem(
        title = formAssignment.form.title,
        subtitle = formatLocalDate(formAssignment.date),
        trailingContent = {
            Text(
                text = "${formAssignment.completionEntries.count()} " + if (formAssignment.completionEntries.count() != 1) stringResource(R.string.resolutions)
                else stringResource(R.string.resolution)
            )
        },
        onClick = {
            navController.navigate(
                onClickRoute.routeTo(formAssignment.id)
            )
        }
    )
}

// TODO hacer reutilizable
@Composable
private fun NoAssignmentsMessage(){
    ImageMessagePage(imageResource = R.drawable.speech_bubble, text = stringResource(id = R.string.patient_has_no_assigned_forms))
}