package com.disfluency.screens.therapist.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.R
import com.disfluency.components.list.item.PatientListItem
import com.disfluency.model.user.Therapist
import com.disfluency.navigation.routing.Route

@Composable
fun NextPatients(therapist: Therapist, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.today_patients),
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )
        val nextPatients = therapist.todayPatients.sortedBy { it.weeklyHour }

        if (nextPatients.isEmpty()){
            Text(
                text = stringResource(R.string.no_more_patients),
                color = Color.Gray,
                fontSize = 13.sp,
                lineHeight = 14.sp
            )
        } else {
            Text(
                text = "Hoy tienes ${therapist.todayPatients.size} pacientes en la agenda",
                color = Color.Gray,
                fontSize = 13.sp,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        nextPatients.forEach { patient ->
            PatientListItem(
                patient = patient,
                onClick = { navController.navigate(Route.Therapist.PatientDetail.routeTo(patient.id)) },
                navController = navController
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}