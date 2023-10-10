package com.disfluency.screens.patient.home.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.utilities.color.verticalGradient

@Composable
fun PendingAssignmentsPanel(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.session_banner_2),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        verticalGradient(Color.Gray)
                    )
            )

            //TODO: mostrar numero de ejercicios pendientes y cuestionarios pendientes (hoy)
            // podriamos hacer que si hay sin responder, que te diga el numero de esos
            // si no, que te diga hace cuantos dias no respondes alguno
            // si ya respondio todos hoy? que mostramos (por ahi un mensaje de ya completaste
            // todo hoy, pero podes seguir practicando)

//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Bottom
//            ) {
//                Text(
//                    text = "Bienvenido",
//                    style = MaterialTheme.typography.displayMedium,
//                    color = Color.White
//                )
//
//                Text(
//                    text = "¡Facilita el tratamiento de la disfluencia completando tus ejercicios y cuestionarios diarios!",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = Color.LightGray
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//            }
        }
    }
}