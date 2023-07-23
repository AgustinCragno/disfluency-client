package com.disfluency.navigation.routing

import com.disfluency.R

sealed class Route(val path: String, val title: Int) {
    object Login: Route("login", R.string.login)

    object Patient {
        object Home: Route("home-patient", R.string.home)
    }

    object Therapist {
        object Home: Route("home-therapist", R.string.home)
        object MyPatients: Route("patients", R.string.my_patients)
        object NewPatient: Route("patients/new", R.string.new_patient)
        object PatientDetail: Route("patients/{id}", R.string.patient){
            fun routeTo(patientId: String): String{
                return path.replace("{id}", patientId)
            }
        }
    }
}

private val ALL_ROUTES = Route::class.sealedSubclasses.map { it.objectInstance as Route }

fun getTitleByRoute(path: String): Int{
    return ALL_ROUTES.first { it.path == path }.title
}

val NO_BOTTOM_BAR_ROUTES = listOf<Route>().map { it.path }
val NO_TOP_BAR_ROUTES = listOf<Route>(Route.Therapist.NewPatient).map { it.path }