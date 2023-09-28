package com.disfluency.api.error

class AssignExerciseException(exerciseIds: List<String>, patientIds: List<String>) : Exception("An error occurred assigning exercises '${exerciseIds}' to patients '${patientIds}'")