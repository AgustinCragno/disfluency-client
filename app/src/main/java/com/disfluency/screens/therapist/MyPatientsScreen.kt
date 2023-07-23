package com.disfluency.screens.therapist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.list.item.PatientListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.PatientListSkeleton
import com.disfluency.model.Patient
import com.disfluency.model.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.viewmodel.PatientsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPatientsScreen(
    therapist: Therapist,
    navController: NavHostController,
    viewModel: PatientsViewModel = viewModel()
){
    LaunchedEffect(Unit){
        viewModel.getPatientsByTherapist(therapist.id)
    }

    var text by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .semantics { isContainer = true }
                    .zIndex(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)) {
                //TODO: ver si se puede esconder el teclado cuando doy enter
                SearchBar(
                    modifier = Modifier.align(Alignment.TopCenter),
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = { },
                    active = false,
                    onActiveChange = { },
                    placeholder = { Text(stringResource(id = R.string.search)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                ) {}
            }

            SkeletonLoader(
                state = viewModel.patients,
                content = {
                    viewModel.patients.value?.let {
                        PatientsList(it, navController, text)
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

@Composable
fun PatientsList(patients: List<Patient>, navController: NavHostController, filter: String) {
    val filteredList = patients.filter { patient -> patient.fullName().contains(filter, true) }

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