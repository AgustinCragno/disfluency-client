package com.disfluency.screens.therapist.exercises

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.components.list.item.ListItem
import com.disfluency.components.thumbnail.ExerciseThumbnail
import com.disfluency.model.exercise.Exercise
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.Route
import com.disfluency.navigation.structure.BottomNavigationScaffold

@Composable
fun MyExercisesScreen(
    therapist: Therapist,
    navController: NavHostController
){
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

                ExerciseList(exercises = therapist.exercises, navController = navController, filter = filterQuery.value)
            }
        }
    }
}

@Composable
private fun NoExercisesMessage() {
    ImageMessagePage(
        imageResource = R.drawable.speech_bubble,
        text = "No tiene ejercicios cargados en el sistema"
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
        leadingContent = {
            ExerciseThumbnail(exercise = exercise)
        },
        onClick = {
            navController.navigate(
                Route.Therapist.ExerciseDetail.routeTo(exercise.id)
            )
        }
    )
}