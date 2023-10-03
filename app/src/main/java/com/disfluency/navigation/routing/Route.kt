package com.disfluency.navigation.routing

sealed class Route(val path: String, private vararg val params: String) {

    fun routeTo(vararg args: String): String{
        return args.foldIndexed(path) { index, argsPath, arg -> argsPath.replace("{${params[index]}}", arg) }
    }

    object Launch: Route("launch")
    object Login: Route("login")
    object SignUpLobby: Route("signup")
    object SignUpPatient: Route("signup/patient")
    object SignUpTherapist: Route("signup/therapist")
    object ConfirmationNewUser: Route("signup/confirmation/therapist")
    object InviteConfirmationPatient: Route("signup/invite-confirmation/{token}", "token")
    object ConfirmationPatient: Route("signup/confirmation/patient")


    object Patient {
        object Home: Route("home-patient")
        object MyExercises: Route("exercise-assignments")
        object MyForms: Route("forms")
        object ExerciseAssignmentDetail: Route("exercise-assignments/{id}", "id")
        object ExercisePractice: Route("exercise-assignments/{id}/practice", "id")
        object RecordConfirmation: Route("exercise-assignments/record-confirmation")
        object FormCompletion: Route("form-assignments/{id}", "id")
        object FormCompletionConfirmation: Route("form-assignments/{id}/response-confirmation", "id")
        object FormCompletionLastEntry: Route("form-assignments/{id}/last-entry", "id")
    }

    object Therapist {
        object Home: Route("home-therapist")
        object MyPatients: Route("patients")

        object MyExercises: Route("exercises")
        object MyForms: Route("forms")
        object NewPatient: Route("patients/new")
        object PatientDetail: Route("patients/{id}", "id")

        object ConfirmationNewPatient: Route("patients/new/confirmation")

        object PatientExercises: Route("patients/{id}/exercises", "id")

        object PatientForms: Route("patients/{id}/forms", "id")

        object PatientSessions: Route("patients/{id}/sessions", "id")

        object ExerciseAssignmentDetail: Route("exercise-assignments/{id}", "id")
        object FormAssignmentDetail: Route("form-assignments/{id}", "id")

        object AnalysisTranscription: Route("analysis/{id}/transcription", "id")
        object AnalysisResults: Route("analysis/{id}/results", "id")
        object NewSession: Route("patients/{id}/sessions/new", "id")
        object NewSessionConfirmation: Route("patients/{id}/sessions/record-confirmation", "id")
        object ExerciseDetail: Route("exercises/{id}", "id")
        object FormDetail: Route("forms/{id}", "id")

        object NewExercise: Route("exercises/new")
        object ConfirmationNewExercise: Route("exercises/new/confirmation")
        object ExerciseAssignmentConfirmation: Route("exercises/assign-confirmation")

        object NewForm: Route("forms/new")
        object ConfirmationNewForm: Route("forms/new/confirmation")
        object FormAssignmentConfirmation: Route("forms/assign-confirmation")
    }
}