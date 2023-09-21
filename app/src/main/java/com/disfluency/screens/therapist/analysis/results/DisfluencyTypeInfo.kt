package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.disfluency.R
import com.disfluency.components.charts.DonutChart
import com.disfluency.model.analysis.AnalysisResults
import com.disfluency.model.analysis.DisfluencyTypeStats
import com.disfluency.utilities.color.darken
import eu.wewox.textflow.TextFlow
import eu.wewox.textflow.TextFlowObstacleAlignment

@Composable
fun DisfluencyTypeInfo(
    disfluencyTypeStats: DisfluencyTypeStats,
    analysisResults: AnalysisResults,
    trailingContent: @Composable () -> Unit = { Divider() }
){
    val type = disfluencyTypeStats.type
    val chartValues = listOf(disfluencyTypeStats.count.toFloat(), analysisResults.totalWords.toFloat())
    val colors = listOf(type.color, Color.LightGray)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){

        val fi = type.fullName
        val indexExplanation = buildAnnotatedString {
            append(stringResource(id = type.descriptionPt1))
            withStyle(style = SpanStyle(color = Color.Black) ) {
                pushStringAnnotation(tag = fi, annotation = fi)
                append(" $fi ")
            }
            append(stringResource(id = type.descriptionPt2))
        }

        TextFlow(
            text = indexExplanation,
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            obstacleAlignment = TextFlowObstacleAlignment.TopStart,
            obstacleContent = {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 12.dp, bottom = 0.dp),
                    text = type.name,
                    style = MaterialTheme.typography.displayMedium,
                    color = type.color
                )
            }
        )

        val percentageText = "${(disfluencyTypeStats.percentageInTotalWords * 100).toString().take(3)}% "

        val percentageOfTotalSection = buildAnnotatedString {
            append(stringResource(R.string.percentage_of_total_words_prefix))
            withStyle(style = SpanStyle(color = Color.Black) ) {
                pushStringAnnotation(tag = percentageText, annotation = percentageText)
                append(percentageText)
            }
            append(stringResource(R.string.percentage_of_total_words_suffix))
        }

        TextFlow(
            text = percentageOfTotalSection,
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
            obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
            obstacleContent = {
                Box(
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 4.dp)
                ){
                    DonutChart(
                        modifier = Modifier.size(60.dp),
                        sliceWidthDp = 10.dp,
                        colors = colors,
                        inputValues = chartValues,
                        centerText = percentageText,
                        textColor = type.color.darken(0.2f),
                        centerTextSize = 14.dp
                    )
                }
            }
        )

        val percentageOfDisfluenciesText = "${(disfluencyTypeStats.percentageInTotalDisfluencies * 100).toInt()}%"

        TextFlow(
            text = stringResource(R.string.percentage_of_total_disfluencies_suffix),
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp),
            obstacleAlignment = TextFlowObstacleAlignment.TopStart,
            obstacleContent = {
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 12.dp, bottom = 0.dp),
                    text = percentageOfDisfluenciesText,
                    style = MaterialTheme.typography.displayMedium,
                    fontSize = 22.sp,
                    color = type.color
                )
            }
        )

        trailingContent()
    }
}