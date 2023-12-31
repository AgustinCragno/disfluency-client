package com.disfluency.screens.patient.forms

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.list.item.FormAssignmentListItem
import com.disfluency.components.list.item.NoFormAssignmentsMessage
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.model.form.FormAssignment
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.viewmodel.FormsViewModel
import java.time.LocalDate


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

            SkeletonLoader(
                state = viewModel.assignedForms,
                content = {
                    viewModel.assignedForms.value?.let { forms ->
                        FormAssignmentList(
                            assignedForms = forms.filter { it.form.title.contains(filterQuery.value, true) },
                            navController = navController
                        )
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
fun FormAssignmentList(assignedForms: List<FormAssignment>, navController: NavHostController){
    if (assignedForms.isEmpty()){
        NoFormAssignmentsMessage()
    }

    val showAlert = remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Log.i("now: ", LocalDate.now().toString())
        items(assignedForms) {assignment ->
            assignment.completionEntries.forEach { Log.i("assignment date: ", it.date.toString()) }
            FormAssignmentListItem(formAssignment = assignment){
                if(assignment.completionEntries.any { it.date == LocalDate.now() }){
                    showAlert.value = true
                }else{
                    navController.navigate(Route.Patient.FormCompletion.routeTo(assignment.id))
                }
            }
        }
    }
    if (showAlert.value){
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(text = stringResource(R.string.form_alert_title))
            },
            text = {
                Text(stringResource(R.string.form_alert_text))
            },
            confirmButton = {
                Button(
                    onClick = { showAlert.value = false },
                    content = {
                        Text(stringResource(R.string.form_alert_button))
                    }
                )
            }
        )
    }
}

