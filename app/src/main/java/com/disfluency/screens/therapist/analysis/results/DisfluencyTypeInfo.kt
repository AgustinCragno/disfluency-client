package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.model.analysis.AnalysisResults
import com.disfluency.model.analysis.DisfluencyTypeStats
import eu.wewox.textflow.TextFlow
import eu.wewox.textflow.TextFlowObstacleAlignment

@Composable
fun DisfluencyTypeInfo(
    disfluencyTypeStats: DisfluencyTypeStats,
    analysisResults: AnalysisResults,
    trailingContent: @Composable () -> Unit = { Divider() }
){
    val chartValues = listOf(disfluencyTypeStats.count.toFloat(), analysisResults.totalWords.toFloat())
    val colors = listOf(disfluencyTypeStats.type.color, Color.LightGray)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){

        //TODO: tener dentro del type el texto de la descripcion
        val fi = disfluencyTypeStats.type.fullName
        val indexExplanation = buildAnnotatedString {
            append("El ")
            withStyle(style = SpanStyle(color = Color.Black) ) {
                pushStringAnnotation(tag = fi, annotation = fi)
                append(fi)
            }
            append(" representa el porcentaje de palabras que fueron pronunciadas de manera correcta con respecto a la cantidad de palabras totales habladas por la persona.\n\nUn indice de fluidez alto significa que la persona pudo comunicarse exitosamente durante gran parte de la sesion.")
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
                    text = disfluencyTypeStats.type.name,
                    style = MaterialTheme.typography.displayMedium,
                    color = disfluencyTypeStats.type.color
                )
            }
        )


        //

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