package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.navigation.structure.BackNavigationScaffold

@Composable
fun PatientFormAssignmentsScreen(patientId: String, navController: NavHostController){
    BackNavigationScaffold(title = "Cuestionarios", navController = navController) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
            ImageMessagePage(imageResource = R.drawable.form_fill, text = stringResource(id = R.string.patient_has_no_assigned_forms))
        }
    }
}