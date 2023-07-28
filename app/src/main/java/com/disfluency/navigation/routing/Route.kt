package com.disfluency.navigation.routing

import com.disfluency.R

sealed class Route(val path: String, private vararg val params: String, val title: Int) {

    fun routeTo(vararg args: String): String{
        return args.foldIndexed(path) { index, argsPath, arg -> argsPath.replace("{${params[index]}}", arg) }
    }

    object Login: Route("login", title = R.string.login)

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
        object PatientDetail: Route("patients/{id}", title = R.string.patient){
            fun routeTo(patientId: String): String{
                return path.replace("{id}", patientId)
            }
        }

        object ConfirmationNewPatient: Route("patients/new/confirmation", title = -1)

        object PatientExercises: Route("patients/{id}/exercises", title = R.string.patient_exercises){
            fun routeTo(patientId: String): String{
                return path.replace("{id}", patientId)
            }
        }

        object PatientForms: Route("patients/{id}/forms", title = R.string.patient_forms){
            fun routeTo(patientId: String): String{
                return path.replace("{id}", patientId)
            }
        }

        object PatientSessions: Route("patients/{id}/sessions", title = R.string.patient_sessions){
            fun routeTo(patientId: String): String{
                return path.replace("{id}", patientId)
            }
        }

        object ExerciseAssignmentDetail: Route("exercise-assignments/{id}", "id", title = R.string.assigned_exercise)
    }
}

private val ALL_ROUTES = Route::class.sealedSubclasses.map { it.objectInstance as Route }

fun getTitleByRoute(path: String): Int{
    return ALL_ROUTES.first { it.path == path }.title
}

val NO_BOTTOM_BAR_ROUTES = listOf<Route>(
    Route.Therapist.ConfirmationNewPatient
).map { it.path }

val NO_TOP_BAR_ROUTES = listOf(
    Route.Therapist.NewPatient,
    Route.Therapist.ConfirmationNewPatient
).map { it.path }