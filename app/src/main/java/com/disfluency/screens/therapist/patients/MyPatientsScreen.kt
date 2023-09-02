package com.disfluency.screens.therapist.patients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.PatientListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.PatientListSkeleton
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.viewmodel.PatientsViewModel

@Composable
fun MyPatientsScreen(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: PatientsViewModel = viewModel()
){
    LaunchedEffect(Unit){
        viewModel.getPatientsByTherapist(therapist.id)
    }

    val filterQuery = rememberSaveable { mutableStateOf("") }

    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Therapist.items(),
        navController = navController
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar(filterQuery = filterQuery)

                SkeletonLoader(
                    state = viewModel.patients,
                    content = {
                        viewModel.patients.value?.let {
                            PatientsList(it, navController, filterQuery.value)
                        }
                    },
                    skeleton = {
                        PatientListSkeleton()
                    }
                )

            }
            PatientCreation(navController)
        }
    }
}

@Composable
fun PatientsList(patients: List<Patient>, navController: NavHostController, filter: String) {
    val filteredList = patients.filter { patient -> patient.fullName().contains(filter, true) }

    if (filteredList.isEmpty()){
        ImageMessagePage(imageResource = R.drawable.avatar_1, text = "No tiene pacientes registrados en el sistema")
    }

    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(filteredList) {patient ->
            PatientListItem(patient, onClick = {
                navController.navigate(Route.Therapist.PatientDetail.routeTo(patient.id))
            })
        }
    }
}



@Composable
fun PatientCreation(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Route.Therapist.NewPatient.path)
            },
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, stringResource(id = R.string.create))
        }
    }
}