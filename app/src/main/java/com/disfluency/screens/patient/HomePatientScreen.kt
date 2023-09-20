package com.disfluency.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.components.bar.HomeTopAppBar
import com.disfluency.model.user.Patient
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.structure.BottomNavigationScaffold
import com.disfluency.viewmodel.LoggedUserViewModel

@Composable
fun HomePatientScreen(patient: Patient, navController: NavHostController, viewModel: LoggedUserViewModel){
    BottomNavigationScaffold(
        bottomNavigationItems = BottomNavigationItem.Patient.items(),
        navController = navController
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .wrapContentSize(Alignment.Center)
        ) {
            HomeTopAppBar(navController = navController, viewModel = viewModel)

            Box(Modifier.fillMaxSize()) {
                Text(
                    text = "Bienvenido",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
}