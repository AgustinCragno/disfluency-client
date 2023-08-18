package com.disfluency.navigation.routing

import com.disfluency.R

sealed class Route(val path: String, private vararg val params: String, val title: Int) {

    fun routeTo(vararg args: String): String{
        return args.foldIndexed(path) { index, argsPath, arg -> argsPath.replace("{${params[index]}}", arg) }
    }

    object Launch: Route("launch", title = R.string.app_name)
    object Login: Route("login", title = R.string.login)
    object SignUpLobby: Route("signup", title = R.string.signup)
    object SignUpPatient: Route("signup/patient", title = R.string.signup)
    object SignUpTherapist: Route("signup/therapist", title = R.string.signup)
    object ConfirmationNewUser: Route("signup/confirmation/therapist", title = -1)
    object InviteConfirmationPatient: Route("signup/invite-confirmation/{token}", "token", title = R.string.signup)
    object ConfirmationPatient: Route("signup/confirmation/patient", title = -1)


    object Patient {
        object Home: Route("home-patient", title = R.string.home)
        object MyExercises: Route("exercise-assignments", title = R.string.my_exercises)
        object ExerciseAssignmentDetail: Route("exercise-assignments/{id}", "id", title = R.string.assigned_exercise)
        object ExercisePractice: Route("exercise-assignments/{id}/practice", "id", title = R.string.practice)
    }

    object Therapist {
        object Home: Route("home-therapist", title = R.string.home)
        object MyPatients: Route("patients", title = R.string.my_patients)
        object NewPatient: Route("patients/new", title = R.string.new_patient)
        object PatientDetail: Route("patients/{id}", "id", title = R.string.patient)

        object ConfirmationNewPatient: Route("patients/new/confirmation", title = -1)

        object PatientExercises: Route("patients/{id}/exercises", "id", title = R.string.patient_exercises)

        object PatientForms: Route("patients/{id}/forms", "id", title = R.string.patient_forms)

        object PatientSessions: Route("patients/{id}/sessions", "id", title = R.string.patient_sessions)

        object ExerciseAssignmentDetail: Route("exercise-assignments/{id}", "id", title = R.string.assigned_exercise)
    }
}

private val ALL_ROUTES = Route::class.sealedSubclasses.map { it.objectInstance as Route }

fun getTitleByRoute(path: String): Int{
    return ALL_ROUTES.first { it.path == path }.title
}

val NO_BOTTOM_BAR_ROUTES = listOf(
    Route.Therapist.ConfirmationNewPatient,
    Route.ConfirmationNewUser,
    Route.ConfirmationPatient
).map { it.path }

val NO_TOP_BAR_ROUTES = listOf(
    Route.Therapist.NewPatient,
    Route.Therapist.ConfirmationNewPatient,
    Route.ConfirmationNewUser,
    Route.ConfirmationPatient
).map { it.path }