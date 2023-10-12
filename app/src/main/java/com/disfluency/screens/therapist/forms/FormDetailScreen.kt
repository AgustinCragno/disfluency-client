package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.dialogs.AssignmentDialog
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.SelectablePatientListItem
import com.disfluency.model.form.Form
import com.disfluency.model.form.FormQuestion
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.AssignmentsViewModel
import com.disfluency.viewmodel.PatientsViewModel
import kotlinx.coroutines.delay

@Composable
fun FormDetailScreen(
    formId: String,
    therapist: Therapist,
    navController: NavHostController,
    patientsViewModel: PatientsViewModel,
    assignmentsViewModel: AssignmentsViewModel
){
    val form = therapist.forms.find { it.id == formId }

    BackNavigationScaffold(
        title = form?.title ?: stringResource(R.string.form),
        navController = navController
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            form?.let {
                FormDetailPanel(form = it)

                FormAssignmentButton(
                    formId = it.id,
                    therapistId = therapist.id,
                    navController = navController,
                    viewModel = patientsViewModel,
                    assignmentsViewModel = assignmentsViewModel
                )
            }
        }
    }
}

@Composable
private fun FormDetailPanel(form: Form){
    val fontSize = 14.sp

    Column(
        modifier = Modifier.padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        form.questions.forEachIndexed { index, question ->

            Text(
                text = "${index + 1}. ${question.scaleQuestion}",
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            ScaleIndicator(question)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.followUpQuestion,
                fontSize = fontSize.times(0.9f),
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ScaleIndicator(question: FormQuestion){
    val scaleFontSize = 13.sp

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = question.minValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(end = 8.dp)
        )

        Box(
            modifier = Modifier.weight(10f),
            contentAlignment = Alignment.Center
        ){
            LinearProgressIndicator(
                progress = 0f,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer,
                strokeCap = StrokeCap.Round
            )
        }

        Text(
            text = question.maxValue,
            fontSize = scaleFontSize,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun FormAssignmentButton(
    formId: String,
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
                        formId = formId,
                        patients = it,
                        selected = selectedPatients
                    )
                }
            },
            dismissAction = dismissAction,
            onSend = {
                assignmentsViewModel.assignFormsToPatients(
                    formIds = listOf(formId),
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
                navController.navigate(Route.Therapist.FormAssignmentConfirmation.path)
            }
        }
    }
}

@Composable
private fun SelectablePatientList(
    formId: String,
    patients: List<Patient>,
    selected: SnapshotStateList<Patient>
){
    if (patients.isEmpty()){
        ImageMessagePage(imageResource = R.drawable.avatar_1, text = stringResource(R.string.doesnt_have_patients_in_system))
    }

    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(patients) {patient ->
            val formAlreadyAssigned = patient.forms.any { it.form.id == formId }

            SelectablePatientListItem(
                patient = patient,
                enabled = !formAlreadyAssigned
            ){
                if (selected.contains(patient)) selected.remove(patient)
                else selected.add(patient)
            }
        }
    }
}