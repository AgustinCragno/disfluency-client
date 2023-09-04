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

//        Text(
//            modifier = Modifier
//                .wrapContentSize()
//                .padding(horizontal = 16.dp, vertical = 2.dp),
//            text = "% sobre el total de palabras",
//            fontSize = 12.sp,
//            lineHeight = 13.sp,
//            color = Color.Black
//        )

        val percentageText = "${(disfluencyTypeStats.percentageInTotalWords * 100).toString().take(3)}% "

        val percentageOfTotalSection = buildAnnotatedString {
            append("Conforma el ")
            withStyle(style = SpanStyle(color = Color.Black) ) {
                pushStringAnnotation(tag = percentageText, annotation = percentageText)
                append(percentageText)
            }
            append("de todas las palabras comunicadas por la persona")
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

//        Text(
//            modifier = Modifier
//                .wrapContentSize()
//                .padding(horizontal = 16.dp, vertical = 2.dp),
//            text = "% sobre el total de disfluencias",
//            fontSize = 12.sp,
//            lineHeight = 13.sp,
//            color = Color.Black
//        )

        val percentageOfDisfluenciesText = "${(disfluencyTypeStats.percentageInTotalDisfluencies * 100).toInt()}%"

        TextFlow(
            text = "del total de las disfluencias producidas fueron de este tipo",
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

//https://dribbble.com/shots/13960806-Business-app

/**
 * Visualizacion de Resultados:
 *
 *      tambien se podria mostrar para cada una (una abajo de la otra)
 *      un panel que diga el nombre completo de la disfluencia, una explicacion breve de
 *      en que consiste, y los 3 datos del DisfluencyStat
 *                                       (count, percentageInTotalWords, percentageInTotalDisfluencies)
 *
 */