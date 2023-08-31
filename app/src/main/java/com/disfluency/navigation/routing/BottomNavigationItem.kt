package com.disfluency.navigation.routing

import android.icu.text.CaseMap.Title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.disfluency.R

sealed class BottomNavigationItem(val route: Route, val icon: ImageVector, val title: Int) {

    object Patient {
        object Home : BottomNavigationItem(Route.Patient.Home, Icons.Outlined.Home, R.string.home)
        object Exercises : BottomNavigationItem(Route.Patient.MyExercises, Icons.Outlined.RecordVoiceOver, R.string.my_exercises)
        object Forms : BottomNavigationItem(Route.Patient.MyForms, Icons.Outlined.Assignment, R.string.my_forms)

        fun items() = listOf(Home, Exercises, Forms)
    }

    object Therapist {
        object Home : BottomNavigationItem(Route.Therapist.Home, Icons.Outlined.Home, R.string.home)
        object Patients : BottomNavigationItem(Route.Therapist.MyPatients, Icons.Outlined.ContactMail, R.string.my_patients)

        fun items() = listOf(Home, Patients)
    }

    companion object {
        val lastVisitedItem: BottomNavigationItem? = null

//        fun getTitleByRoute(path: String): Int{
//
//            return ALL_ROUTES.first { it.path == path }.title
//        }
    }
}


