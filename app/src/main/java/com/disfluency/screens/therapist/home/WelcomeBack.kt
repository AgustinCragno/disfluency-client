package com.disfluency.screens.therapist.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.icon.IconLabeled
import com.disfluency.data.mock.MockedData
import com.disfluency.model.user.Patient
import com.disfluency.model.user.Therapist
import com.disfluency.utilities.format.formatWeeklyTurn
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeBack(therapist: Therapist) {

    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Row(
            modifier = Modifier
                .height(122.dp)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = therapist.avatar()),
                contentDescription = "User Thumbnail",
                modifier = Modifier.size(90.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¡Bienvenido ${therapist.name}!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow {
                    IconLabeled(
                        icon = Icons.Outlined.CalendarMonth,
                        label = "Hoy tienes ${therapist.todayPatients.size} pacientes en la agenda."
                    )

//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    IconLabeled(
//                        icon = Icons.Outlined.AccessTime,
//                        label = patient.weeklyHour.format(
//                            DateTimeFormatter.ofPattern(
//                                stringResource(
//                                    R.string.time_format)
//                            ))
//                    )
                }
            }
        }
    }


}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PatientInfoCard(patient: Patient){
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .height(122.dp)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = patient.avatar()),
                contentDescription = "User Thumbnail",
                modifier = Modifier.size(90.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = patient.fullName(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                FlowRow {
                    IconLabeled(
                        icon = Icons.Outlined.CalendarMonth,
                        label = formatWeeklyTurn(patient.weeklyTurn)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconLabeled(
                        icon = Icons.Outlined.AccessTime,
                        label = patient.weeklyHour.format(
                            DateTimeFormatter.ofPattern(
                                stringResource(
                                R.string.time_format)
                            ))
                    )
                }
            }
        }
    }
}
