package com.disfluency.navigation.routing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContactMail
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(val route: Route, val icon: ImageVector) {

    object Patient {
        object Home : BottomNavigationItem(Route.Patient.Home, Icons.Outlined.Home)

        fun items(): List<BottomNavigationItem>{
            return this::class.nestedClasses.map { it.objectInstance as BottomNavigationItem }
        }
    }

    object Therapist {
        object Home : BottomNavigationItem(Route.Therapist.Home, Icons.Outlined.Home)
        object Patients : BottomNavigationItem(Route.Therapist.Patients, Icons.Outlined.ContactMail)

        fun items(): List<BottomNavigationItem>{
            return this::class.nestedClasses.map { it.objectInstance as BottomNavigationItem }
        }
    }
}