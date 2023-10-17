package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.dialogs.AssignmentDialog
import com.disfluency.components.icon.IconLabeled
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.list.item.SelectableListItemColors
import com.disfluency.components.list.item.SelectablePatientListItem
import com.disfluency.components.skeleton.SkeletonLoader
import com.disfluency.components.skeleton.list.ExerciseAssignmentListSkeleton
import com.disfluency.components.thumbnail.TitleThumbnail
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.exercise.ExerciseAssignment
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.utilities.color.stringToRGB
import com.disfluency.utilities.format.formatLocalDate
import com.disfluency.viewmodel.AssignmentsViewModel
import com.disfluency.viewmodel.ExercisesViewModel
import kotlinx.coroutines.delay

@Composable
fun PatientExerciseAssignmentsScreen(
    patientId: String,
    exercises: List<Exercise>,
    navController: NavHostController,
    viewModel: ExercisesViewModel = viewModel(),
    assignmentsViewModel: AssignmentsViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.getAssignmentsOfPatient(patientId)
    }


    BackNavigationScaffold(
        title = stringResource(R.string.patient_exercises),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SkeletonLoader(
                state = viewModel.assignments,
                content = {
                    viewModel.assignments.value?.let {
                        ExerciseAssignmentList(
                            exerciseAssignments = it,
                            navController = navController,
                            onClickRoute = Route.Therapist.ExerciseAssignmentDetail
                        )
                    }
                },
                skeleton = {
                    ExerciseAssignmentListSkeleton()
                }
            )

            ExerciseAssignmentButton(
                patientId = patientId,
                exercises = exercises,
                navController = navController,
                viewModel = viewModel,
                assignmentsViewModel = assignmentsViewModel
            )
        }
    }
}

@Composable
fun ExerciseAssignmentList(exerciseAssignments: List<ExerciseAssignment>, navController: NavHostController, onClickRoute: Route){
    if (exerciseAssignments.isEmpty()){
        NoAssignmentsMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(exerciseAssignments) {ex ->
            ExerciseAssignmentListItem(exerciseAssignment = ex, navController = navController, onClickRoute = onClickRoute)
        }
    }
}

@Composable
fun ExerciseAssignmentListItem(exerciseAssignment: ExerciseAssignment, navController: NavHostController, onClickRoute: Route){

    ListItem(
        title = exerciseAssignment.exercise.title,
        subtitle = formatLocalDate(exerciseAssignment.dateOfAssignment),
        leadingContent = {
            TitleThumbnail(exerciseAssignment.exercise.title)
        },
        trailingContent = {
            val color = if (exerciseAssignment.attemptsCount() > 0) MaterialTheme.colorScheme.primary else Color.Gray

            IconLabeled(
                icon = Icons.Default.Repeat,
                label = exerciseAssignment.attemptsCount().toString(),
                iconColor = color,
                labelColor = color,
                labelSize = 15.sp
            )
        },
        onClick = {
            navController.navigate(
                onClickRoute.routeTo(exerciseAssignment.id)
            )
        }
    )
}

@Composable
private fun NoAssignmentsMessage(){
    ImageMessagePage(imageResource = R.drawable.speech_bubble, text = stringResource(id = R.string.patient_has_no_assigned_exercises))
}



@Composable
private fun ExerciseAssignmentButton(
    patientId: String,
    exercises: List<Exercise>,
    navController: NavHostController,
    viewModel: ExercisesViewModel,
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
        val exercisesState: MutableState<List<Exercise>?> = remember { mutableStateOf(exercises) }
        val selectedExercises = remember { mutableStateListOf<Exercise>() }

        var send by remember { mutableStateOf(false) }

        AssignmentDialog(
            contentState = exercisesState,
            content = {
                SelectableExerciseList(
                    exercises = exercises,
                    patientAssignments = viewModel.assignments.value!!,
                    selected = selectedExercises
                )
            },
            dismissAction = dismissAction,
            onSend = {
                assignmentsViewModel.assignExercisesToPatients(
                    exerciseIds = selectedExercises.map { it.id }.toList(),
                    patientIds = listOf(patientId)
                )
                send = true
            },
            sendEnabled = selectedExercises.isNotEmpty(),
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
private fun SelectableExerciseList(
    exercises: List<Exercise>,
    patientAssignments: List<ExerciseAssignment>,
    selected: SnapshotStateList<Exercise>
){
    if (exercises.isEmpty()){
        ImageMessagePage(imageResource = R.drawable.avatar_1, text = stringResource(R.string.doesnt_have_exercises_in_system))
    }

    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(exercises) {exercise ->
            val exerciseAlreadyAssigned = patientAssignments.any { it.exercise.id == exercise.id }

            SelectableExerciseListItem(
                exercise = exercise,
                enabled = !exerciseAlreadyAssigned
            ){
                if (selected.contains(exercise)) selected.remove(exercise)
                else selected.add(exercise)
            }
        }
    }
}

@Composable
private fun SelectableExerciseListItem(exercise: Exercise, enabled: Boolean, onClick: () -> Unit){
    var selected by remember {
        mutableStateOf(false)
    }

    val colors = SelectableListItemColors.getColorsFromState(selected, enabled)

    ListItem(
        title = exercise.title,
        subtitle = exercise.instruction,
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
                TitleThumbnail(exercise.title)
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