package com.disfluency.screens.patient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.bar.SearchBar
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.structure.BottomNavigationScaffold

@Composable
fun MyFormsScreen(patient: Patient, navController: NavHostController){
    val filterQuery = remember { mutableStateOf("") }

    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Patient.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(filterQuery = filterQuery)

            ImageMessagePage(imageResource = R.drawable.form_fill, text = stringResource(id = R.string.patient_has_no_assigned_forms))
        }
    }
}