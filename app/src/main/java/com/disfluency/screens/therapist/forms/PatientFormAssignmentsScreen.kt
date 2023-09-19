package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.model.form.FormAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.patient.forms.FormAssignmentListItem
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.FormsViewModel

@Composable
fun PatientFormAssignmentsScreen(
    patientId: String,
    navController: NavHostController,
    viewModel: FormsViewModel
){
    LaunchedEffect(Unit){
        viewModel.getAssignmentsOfPatient(patientId)
    }

    BackNavigationScaffold(
        title = stringResource(R.string.patient_forms),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SkeletonLoader(
                state = viewModel.assignedForms,
                content = {
                    viewModel.assignedForms.value?.let {
                        FormAssignmentsList(assignments = it, navController = navController)
                    }
                },
                skeleton = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }
                }
            )
//            viewModel.assignedForms.value?.let {
//                FormAssignmentsList(assignments = it, navController = navController)
//            }
//                ?:
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ){
//                CircularProgressIndicator()
//            }
        }
    }
}

@Composable
fun FormAssignmentsList(
    assignments: List<FormAssignment>,
    navController: NavHostController
){
    if (assignments.isEmpty()){
        NoAssignmentsMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignments) {
            FormAssignmentListItem(
                formAssignment = it,
                navController = navController
            )
        }
    }
}

@Composable
fun FormAssignmentListItem(formAssignment: FormAssignment, navController: NavHostController){
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
            if (formAssignment.completionEntries.isNotEmpty()){
                navController.navigate(
                    Route.Therapist.FormAssignmentDetail.routeTo(formAssignment.id)
                )
            }
        }
    )
}

@Composable
private fun NoAssignmentsMessage(){
    ImageMessagePage(
        imageResource = R.drawable.form_fill,
        text = stringResource(id = R.string.patient_has_no_assigned_forms)
    )
}