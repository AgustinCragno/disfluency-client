package com.disfluency.screens.patient.home.carousel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R
import com.disfluency.components.list.item.CarouselCard
import com.disfluency.components.text.NumberTag
import com.disfluency.model.user.Patient
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.verticalGradient
import com.disfluency.utilities.color.verticalGradientStrong
import java.time.LocalDateTime

private const val GOOD_WORK_COUNT = 7

@Composable
fun LastTwoWeeksRecapPanel(
    patient: Patient
){
    val exercisesCompletedInLastTwoWeeks = patient.exercises
        .flatMap { it.practiceAttempts }
        .count { it.date.isAfter(LocalDateTime.now().minusWeeks(2)) }

    CarouselCard(
        background = R.drawable.form_banner_2,
        gradient = verticalGradientStrong(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row() {
                NumberTag(
                    number = exercisesCompletedInLastTwoWeeks.toString(),
                    modifier = Modifier
                        .size(35.dp)
                        .offset(y = 8.dp),
                    fontSize = 20.sp,
                    color = Color.Yellow.darken(0.25f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.exercises_completed_in_last_two_weeks),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontSize = 20.sp,
                    lineHeight = 22.sp
                )
            }

            Text(
                text = stringResource(
                    if (exercisesCompletedInLastTwoWeeks > GOOD_WORK_COUNT)
                        R.string.last_two_weeks_good_work
                    else
                        R.string.last_two_weeks_lets_get_to_work
                ),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))
        }


    }
}