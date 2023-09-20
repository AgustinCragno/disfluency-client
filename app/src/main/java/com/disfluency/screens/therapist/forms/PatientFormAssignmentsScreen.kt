package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.list.item.FormAssignmentListItem
import com.disfluency.components.list.item.NoFormAssignmentsMessage
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.model.form.FormAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
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
        }
    }
}

@Composable
fun FormAssignmentsList(
    assignments: List<FormAssignment>,
    navController: NavHostController
){
    if (assignments.isEmpty()){
        NoFormAssignmentsMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(assignments) {
            FormAssignmentListItem(
                formAssignment = it,
                onClick = {
                    if (it.completionEntries.isNotEmpty()){
                        navController.navigate(Route.Therapist.FormAssignmentDetail.routeTo(it.id))
                    }
                }
            )
        }
    }
}


