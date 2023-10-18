package com.disfluency.screens.patient.home.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R
import com.disfluency.components.list.item.CarouselCard
import com.disfluency.components.text.NumberTag
import com.disfluency.model.user.Patient
import com.disfluency.model.user.PatientProgressInfo
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.lighten
import com.disfluency.utilities.color.verticalGradient
import com.disfluency.utilities.color.verticalGradientStrong
import com.disfluency.utilities.date.isToday

@Composable
fun PendingAssignmentsPanel(
    progressInfo: PatientProgressInfo
){
    if (progressInfo.allAssignmentsCompletedToday){
        AllAssignmentsCompletedForTodayPanel(progressInfo)
    } else {
        LetsGetToWorkPanel(progressInfo)
    }
}

@Composable
private fun AllAssignmentsCompletedForTodayPanel(
    progressInfo: PatientProgressInfo
){
    CarouselCard(
        background = R.drawable.assignments_done_2,
        gradient = verticalGradientStrong(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.done_for_today),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )

            Text(
                text = stringResource(R.string.you_have_completed_all_assignments_today),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                CompletedCountIndicator(
                    title = stringResource(id = R.string.exercises).lowercase(),
                    total = progressInfo.exercisesCount,
                    completed = progressInfo.exercisesCount,
                    color = MaterialTheme.colorScheme.primary
                )

                CompletedCountIndicator(
                    title = stringResource(id = R.string.forms).lowercase(),
                    total = progressInfo.formsCount,
                    completed = progressInfo.formsCount,
                    color = Color.Red.lighten()
                )
            }

        }
    }

}

@Composable
private fun CompletedCountIndicator(
    title: String,
    total: Int,
    completed: Int,
    color: Color = MaterialTheme.colorScheme.primary
){
    Column(
        horizontalAlignment = CenterHorizontally
    ) {
        NumberTag(
            number = "$completed/$total",
            fontSize = 18.sp,
            color = color
        )

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
private fun LetsGetToWorkPanel(
    progressInfo: PatientProgressInfo
){
    CarouselCard(
        background = R.drawable.session_banner_2,
        gradient = verticalGradient(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.secondary
                        .copy(alpha = 0.3f)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.lets_get_to_work),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )

            Text(
                text = stringResource(R.string.lets_get_to_work_description),
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                CompletedCountIndicator(
                    title = stringResource(id = R.string.exercises).lowercase(),
                    total = progressInfo.exercisesCount,
                    completed = progressInfo.exercisesCompletedTodayCount,
                    color = Color.Green.darken()
                )

                CompletedCountIndicator(
                    title = stringResource(id = R.string.forms).lowercase(),
                    total = progressInfo.formsCount,
                    completed = progressInfo.formsCompletedTodayCount,
                    color = Color.Blue.darken()
                )
            }
        }
    }
}