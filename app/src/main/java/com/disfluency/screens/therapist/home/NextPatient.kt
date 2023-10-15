package com.disfluency.screens.therapist.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.disfluency.components.list.item.PatientListItem
import com.disfluency.data.mock.MockedData
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
            text = "Próximos pacientes",
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        therapist.todayPatients.maxByOrNull { it.weeklyHour }?.let {
            PatientListItem(
                patient = it,
                onClick = { navController.navigate(Route.Therapist.PatientDetail.routeTo(it.id)) },
                sessionShortcut = true,
                navController = navController
            )
        }
        if (therapist.getNextPatients().isEmpty()){
            Text(
                text = "No tienes mas pacientes por hoy. ¡Gran trabajo!",
                color = Color.Gray,
                fontSize = 13.sp,
                lineHeight = 14.sp
            )
        }
    }
}
