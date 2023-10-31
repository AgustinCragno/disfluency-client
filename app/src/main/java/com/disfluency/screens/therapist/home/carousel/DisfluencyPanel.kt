package com.disfluency.screens.therapist.home.carousel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.list.item.CarouselCard
import com.disfluency.utilities.color.verticalGradient

@Composable
fun TherapistDisfluencyPanel(){
    CarouselCard(
        background = R.drawable.session_banner_2,
        gradient = verticalGradient(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )

            Text(
                text = "Optimiza el tratamiento de la tartamudez a traves del analisis automatico y el trabajo remoto con tus pacientes",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}