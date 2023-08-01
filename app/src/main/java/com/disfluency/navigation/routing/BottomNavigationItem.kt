package com.disfluency.navigation.routing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContactMail
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(val route: Route, val icon: ImageVector) {

    object Patient {
        object Home : BottomNavigationItem(Route.Patient.Home, Icons.Outlined.Home)
        object Exercises : BottomNavigationItem(Route.Patient.MyExercises, Icons.Outlined.RecordVoiceOver)

        fun items() = listOf(Home, Exercises)
    }

    object Therapist {
        object Home : BottomNavigationItem(Route.Therapist.Home, Icons.Outlined.Home)
        object Patients : BottomNavigationItem(Route.Therapist.MyPatients, Icons.Outlined.ContactMail)

        fun items() = listOf(Home, Patients)
    }
}