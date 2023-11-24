package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.thumbnail.TitleThumbnail
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.viewmodel.ExercisesViewModel

@Composable
fun MyExercisesScreen(
    therapist: Therapist,
    navController: NavHostController,
    exercisesViewModel: ExercisesViewModel
){
    val filterQuery = rememberSaveable { mutableStateOf("") }
    LaunchedEffect(Unit){
        exercisesViewModel.getExercisesByTherapistId(therapist.id)
    }

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
                exercisesViewModel.exercises.value?.let {
                    ExerciseList(exercises = it, navController = navController, filter = filterQuery.value)
                }
            }

            ExerciseCreationButton(navController = navController)
        }
    }
}

@Composable
private fun NoExercisesMessage() {
    ImageMessagePage(
        imageResource = R.drawable.speech_bubble,
        text = stringResource(R.string.doesnt_have_exercises_in_system)
    )
}

@Composable
private fun ExerciseList(exercises: List<Exercise>, navController: NavHostController, filter: String){
    val filteredList = exercises.filter { it.title.contains(filter, true)}

    if (filteredList.isEmpty()){
        NoExercisesMessage()
    }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filteredList) {ex ->
            ExerciseListItem(exercise = ex, navController = navController)
        }
    }
}

@Composable
private fun ExerciseListItem(exercise: Exercise, navController: NavHostController){
    ListItem(
        title = exercise.title,
        subtitle = exercise.instruction,
        leadingContent = {
            TitleThumbnail(exercise.title)
        },
        onClick = {
            navController.navigate(
                Route.Therapist.ExerciseDetail.routeTo(exercise.id)
            )
        }
    )
}

@Composable
private fun ExerciseCreationButton(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate(Route.Therapist.NewExercise.path)
            },
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, stringResource(id = R.string.create))
        }
    }
}