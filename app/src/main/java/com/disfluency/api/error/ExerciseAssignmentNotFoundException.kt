package com.disfluency.api.error

class ExerciseAssignmentNotFoundException(id: String) : Exception("Assignment with id '$id' could not be found")