package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.dialogs.AssignmentDialog
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.FormAssignmentListItem
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.list.item.NoFormAssignmentsMessage
import com.disfluency.components.list.item.SelectableListItemColors
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.thumbnail.TitleThumbnail
import com.disfluency.model.form.Form
import com.disfluency.model.form.FormAssignment
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.viewmodel.AssignmentsViewModel
import com.disfluency.viewmodel.FormsViewModel
import kotlinx.coroutines.delay

@Composable
fun PatientFormAssignmentsScreen(
    patientId: String,
    forms: List<Form>,
    navController: NavHostController,
    viewModel: FormsViewModel,
    assignmentsViewModel: AssignmentsViewModel
){
    LaunchedEffect(Unit){
        viewModel.getAssignmentsOfPatient(patientId)
    }

    BackNavigationScaffold(
        title = stringResource(R.string.patient_forms),
        navController = navController
    ) { paddingValues ->
        Box(
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

            FormAssignmentButton(
                patientId = patientId,
                forms = forms,
                navController = navController,
                viewModel = viewModel,
                assignmentsViewModel = assignmentsViewModel
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



@Composable
private fun FormAssignmentButton(
    patientId: String,
    forms: List<Form>,
    navController: NavHostController,
    viewModel: FormsViewModel,
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
            containerColor = MaterialTheme.colorScheme.secondary,
        ) {
            Icon(Icons.Filled.Send, null)
        }
    }

    if (openDialog){
        val formsState: MutableState<List<Form>?> = remember { mutableStateOf(forms) }
        val selectedForms = remember { mutableStateListOf<Form>() }

        var send by remember { mutableStateOf(false) }

        AssignmentDialog(
            contentState = formsState,
            content = {
                SelectableFormList(
                    forms = forms,
                    patientAssignments = viewModel.assignedForms.value!!,
                    selected = selectedForms
                )
            },
            dismissAction = dismissAction,
            onSend = {
                assignmentsViewModel.assignFormsToPatients(
                    formIds = selectedForms.map { it.id }.toList(),
                    patientIds = listOf(patientId)
                )
                send = true
            },
            sendEnabled = selectedForms.isNotEmpty(),
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
private fun SelectableFormList(
    forms: List<Form>,
    patientAssignments: List<FormAssignment>,
    selected: SnapshotStateList<Form>
){
    if (forms.isEmpty()){
        ImageMessagePage(imageResource = R.drawable.avatar_1, text = stringResource(R.string.doesnt_have_forms_in_system))
    }

    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(forms) { form ->
            val exerciseAlreadyAssigned = patientAssignments.any { it.form.id == form.id }

            SelectableFormListItem(
                form = form,
                enabled = !exerciseAlreadyAssigned
            ){
                if (selected.contains(form)) selected.remove(form)
                else selected.add(form)
            }
        }
    }
}

@Composable
private fun SelectableFormListItem(form: Form, enabled: Boolean, onClick: () -> Unit){
    var selected by remember {
        mutableStateOf(false)
    }

    val colors = SelectableListItemColors.getColorsFromState(selected, enabled)

    ListItem(
        title = form.title,
        leadingContent = {
            if (selected || !enabled){
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = colors.icon,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                )
            } else {
                TitleThumbnail(form.title)
            }
        },
        colors = ListItemDefaults.colors(colors.background),
        onClick = {
            if (enabled) {
                selected = !selected
                onClick()
            }
        }
    )
}

