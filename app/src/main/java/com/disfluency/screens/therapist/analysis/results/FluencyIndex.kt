package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R
import com.disfluency.components.charts.DonutChart
import com.disfluency.model.analysis.AnalysisResults
import eu.wewox.textflow.TextFlow
import eu.wewox.textflow.TextFlowObstacleAlignment


@Composable
fun FluencyIndexChart(analysisResults: AnalysisResults){
    val chartValues = listOf(
        analysisResults.cleanWordsCount.toFloat(),
        analysisResults.totalDisfluencies.toFloat()
    )

    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    )

    val fluencyIndex = (analysisResults.fluencyIndex * 100).toInt()

    val chartPadding = 24.dp

    StatsPanel(
        modifier = Modifier.wrapContentWidth(),
        title = stringResource(R.string.fluency)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fi = stringResource(R.string.fluency_index)
            val indexExplanation = buildAnnotatedString {
                append(stringResource(R.string.fluency_index_prefix))
                withStyle(style = SpanStyle(color = Color.Black) ) {
                    pushStringAnnotation(tag = fi, annotation = fi)
                    append(fi)
                }
                append(stringResource(R.string.fluency_index_suffix))
            }

            TextFlow(
                text = indexExplanation,
                color = Color.Gray,
                fontSize = 12.sp,
                lineHeight = 13.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(chartPadding),
                obstacleAlignment = TextFlowObstacleAlignment.TopStart,
                obstacleContent = {
                    Box(modifier = Modifier.padding(end = chartPadding, bottom = chartPadding.times(0.6f))){
                        DonutChart(
                            modifier = Modifier.size(120.dp),
                            sliceWidthDp = 20.dp,
                            colors = colors,
                            inputValues = chartValues,
                            centerText = "$fluencyIndex%",
                            textColor = Color.Black
                        )
                    }
                }
            )

            Column() {
                StatLabel(
                    title = stringResource(R.string.total_words),
                    subtitle = stringResource(R.string.total_words_spoken_in_session),
                    number = analysisResults.totalWords,
                    leadingContent = {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Outlined.Textsms,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                )

                StatLabel(
                    title = stringResource(R.string.clean_words),
                    subtitle = stringResource(R.string.pronounced_correctly),
                    number = analysisResults.cleanWordsCount,
                    indicator = colors[0]
                )

                StatLabel(
                    title = stringResource(R.string.disfluencies_lc),
                    subtitle = stringResource(R.string.errors_in_comunication),
                    number = analysisResults.totalDisfluencies,
                    indicator = colors[1]
                )
            }
        }
    }
}
