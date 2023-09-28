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
import com.disfluency.components.dialogs.AnimatedDialog
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.SelectablePatientListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.PatientListSkeleton
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.PatientsViewModel

@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    therapist: Therapist,
    navController: NavHostController,
    viewModel: PatientsViewModel
){
    val exercise = therapist.exercises.find { it.id == exerciseId }

    LaunchedEffect(Unit){
        if (viewModel.patients.value == null){
            viewModel.getPatientsByTherapist(therapist.id)
        }
    }

    BackNavigationScaffold(
        title = stringResource(R.string.exercise),
        navController = navController
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            exercise?.let {
                ExerciseDetailPanelFixed(exercise = it)

                ExerciseAssignmentButton(exerciseId = it.id, navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun ExerciseAssignmentButton(exerciseId: String, navController: NavHostController, viewModel: PatientsViewModel) {
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
        AssignmentDialog(
            exerciseId = exerciseId,
            navController = navController,
            viewModel = viewModel,
            dismissAction = dismissAction
        )
    }
}

@Composable
private fun AssignmentDialog(
    exerciseId: String,
    navController: NavHostController,
    viewModel: PatientsViewModel,
    dismissAction: () -> Unit
){
    val selectedPatients = remember {
        mutableStateListOf<Patient>()
    }

    AnimatedDialog(dismissAction = dismissAction) {
        Surface(
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = stringResource(R.string.assign_to),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(10f)
                ) {
                    SkeletonLoader(
                        state = viewModel.patients,
                        content = {
                            viewModel.patients.value?.let {
                                SelectablePatientList(
                                    exerciseId = exerciseId,
                                    patients = it,
                                    selected = selectedPatients
                                )
                            }
                        },
                        skeleton = {
                            PatientListSkeleton()
                        }
                    )
                }

                SendAndCancelButtons(
                    modifier = Modifier.weight(1f),
                    onCancel = dismissAction,
                    sendEnabled = selectedPatients.isNotEmpty(),
                    onSend = {
                        //TODO: enviar
                    }
                )
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

@Composable
private fun SendAndCancelButtons(
    modifier: Modifier = Modifier,
    sendEnabled: Boolean,
    onCancel: () -> Unit,
    onSend: () -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = onCancel,
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }

        TextButton(
            modifier = Modifier
                .weight(1f),
            onClick = onSend,
            enabled = sendEnabled
        ) {
            Text(text = stringResource(id = R.string.send))
        }
    }
}