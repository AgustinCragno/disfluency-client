package com.disfluency.navigation.routing

import com.disfluency.R

sealed class Route(val path: String, val title: Int) {
    object Login: Route("login", R.string.login)

    object Patient {
        object Home: Route("home-patient", R.string.home)
    }

    object Therapist {
        object Home: Route("home-therapist", R.string.home)
        object Patients: Route("patients", R.string.my_patients)
    }
}

private val ALL_ROUTES = Route::class.nestedClasses.flatMap {
    val instance = it.objectInstance
    if (instance is Route) listOf(instance)
    else {
        instance!!::class.nestedClasses.map { ob -> ob.objectInstance as Route }
    }
}

fun getTitleByRoute(path: String): Int{
    return ALL_ROUTES.first { it.path == path }.title
}

val NO_BOTTOM_BAR_ROUTES = listOf<Route>().map { it.path }
val NO_TOP_BAR_ROUTES = listOf<Route>().map { it.path }