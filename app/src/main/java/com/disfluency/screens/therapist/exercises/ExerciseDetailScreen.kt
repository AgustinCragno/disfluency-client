package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.button.SendAndCancelButtons
import com.disfluency.components.dialogs.AnimatedDialog
import com.disfluency.components.dialogs.AssignmentDialog
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.SelectablePatientListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.PatientListSkeleton
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.AssignmentsViewModel
import com.disfluency.viewmodel.PatientsViewModel
import kotlinx.coroutines.delay

@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    therapist: Therapist,
    navController: NavHostController,
    viewModel: PatientsViewModel,
    assignmentsViewModel: AssignmentsViewModel
){
    val exercise = therapist.exercises.find { it.id == exerciseId }

    BackNavigationScaffold(
        title = stringResource(R.string.exercise),
        navController = navController
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            exercise?.let {
                ExerciseDetailPanelFixed(exercise = it)

                ExerciseAssignmentButton(
                    exerciseId = it.id,
                    therapistId = therapist.id,
                    navController = navController,
                    viewModel = viewModel,
                    assignmentsViewModel = assignmentsViewModel
                )
            }
        }
    }
}

@Composable
private fun ExerciseAssignmentButton(
    exerciseId: String,
    therapistId: String,
    navController: NavHostController,
    viewModel: PatientsViewModel,
    assignmentsViewModel: AssignmentsViewModel
) {
    var openDialog by remember { mutableStateOf(false) }

    val dismissAction = { openDialog = false }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                openDialog = true
            },
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Send, null)
        }
    }

    if (openDialog){
        val selectedPatients = remember { mutableStateListOf<Patient>() }

        var send by remember { mutableStateOf(false) }

        AssignmentDialog(
            contentState = viewModel.patients,
            content = {
                viewModel.patients.value?.let {
                    SelectablePatientList(
                        exerciseId = exerciseId,
                        patients = it,
                        selected = selectedPatients
                    )
                }
            },
            dismissAction = dismissAction,
            onSend = {
                assignmentsViewModel.assignExercisesToPatients(
                    exerciseIds = listOf(exerciseId),
                    patientIds = selectedPatients.map { it.id }
                )
                send = true
            },
            sendEnabled = selectedPatients.isNotEmpty(),
            onLaunch = {
                if (viewModel.patients.value == null){
                    viewModel.getPatientsByTherapist(therapistId)
                }
            }
        )

        if (send){
            LaunchedEffect(Unit){
                delay(200)
                dismissAction()
                navController.navigate(Route.Therapist.ExerciseAssignmentConfirmation.path)
            }
        }
    }
}

@Composable
private fun SelectablePatientList(
    exerciseId: String,
    patients: List<Patient>,
    selected: SnapshotStateList<Patient>
){
    if (patients.isEmpty()){
        ImageMessagePage(imageResource = R.drawable.avatar_1, text = stringResource(R.string.doesnt_have_patients_in_system))
    }

    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(patients) {patient ->
            val exerciseAlreadyAssigned = patient.exercises.any { it.exercise.id == exerciseId }

            SelectablePatientListItem(
                patient = patient,
                enabled = !exerciseAlreadyAssigned
            ){
                if (selected.contains(patient)) selected.remove(patient)
                else selected.add(patient)
            }
        }
    }
}
