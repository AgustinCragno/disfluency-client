package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WrapText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
fun DisfluenciesPerPhraseChart(analysisResults: AnalysisResults){
    StatsPanel(title = stringResource(R.string.disfluencies_per_phrase)) {
        val chartPadding = 24.dp

        Column() {
            val adpf = stringResource(R.string.average_disfluencies_per_phrase)
            val adpfExplanations = buildAnnotatedString {
                append(stringResource(R.string.average_disfluencies_per_phrase_prefix))
                withStyle(style = SpanStyle(color = Color.Black) ) {
                    pushStringAnnotation(tag = adpf, annotation = adpf)
                    append(adpf)
                }
                append(stringResource(R.string.average_disfluencies_per_phrase_suffix))
            }

            TextFlow(
                text = adpfExplanations,
                color = Color.Gray,
                fontSize = 12.sp,
                lineHeight = 13.sp,
//                textAlign = TextAlign.Justify,
//                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(chartPadding),
                obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
                obstacleContent = {
                    Box(modifier = Modifier.padding(start = chartPadding, bottom = chartPadding.times(0.6f))){
                        DonutChart(
                            modifier = Modifier.size(120.dp),
                            sliceWidthDp = 10.dp,
                            colors = listOf(Color.Blue.copy(alpha = 0.25f)),
                            inputValues = listOf(1f),
                            centerText = "${analysisResults.avgDisfluenciesPerPhrase.toString().take(3)}  ",
                            textColor = Color.Blue
                        )
                    }
                }
            )

            StatLabel(
                title = stringResource(R.string.total_phrases),
                subtitle = stringResource(R.string.completed_by_patient),
                number = analysisResults.totalPhrases
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Outlined.WrapText,
                    contentDescription = null,
                    tint = Color.Blue
                )
            }
        }
    }
}