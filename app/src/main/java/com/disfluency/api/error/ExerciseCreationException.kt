package com.disfluency.api.error

import com.disfluency.api.dto.NewExerciseDTO

class ExerciseCreationException(newExercise: NewExerciseDTO) : Exception("An error occurred when creating exercise '${newExercise.title}'")