package com.disfluency.screens.therapist.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.Rtt
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Textsms
import androidx.compose.material.icons.outlined.Textsms
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.disfluency.components.charts.DonutChart
import com.disfluency.data.mock.MockedData
import com.disfluency.model.analysis.AnalysisResults
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.ui.theme.DisfluencyTheme
import eu.wewox.textflow.TextFlow
import eu.wewox.textflow.TextFlowObstacleAlignment


@Preview
@Composable
private fun ResultsScreenPreview(){
    DisfluencyTheme() {
        AnalysisResultsScreen(
            analysisResults = MockedData.analysisResults,
            navController = rememberNavController()
        )
    }
}

@Composable
fun AnalysisResultsScreen(
    analysisResults: AnalysisResults, //TODO: temp, recibir id e ir a buscarlo al viewModel
    navController: NavHostController
){
    BackNavigationScaffold(
        title = "Analisis de Disfluencias",
        navController = navController
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                FluencyIndexChart(analysisResults = analysisResults)
            }
        }
    }
}

@Composable
private fun StatsPanel(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
){
    Card(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .background(Color.LightGray.copy(alpha = 0.15f))
                    .fillMaxWidth()
            ) {
                val fontSize = 18.sp
                val padding = 12.dp

                Text(
                    text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSize,
                    modifier = Modifier.padding(padding)
                )
            }

            Divider(
                thickness = 3.dp,
                color = Color.Gray.copy(alpha = 0.3f)
            )

            content()
        }
    }
}

@Composable
private fun StatLabel(title: String, subtitle: String, number: Number, leadingContent: @Composable () -> Unit){
    val fontSize = 15.sp

    Divider()

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent()

        Spacer(modifier = Modifier.width(12.dp))

        Column() {
            Text(
                text = title,
                color = Color.Black,
                fontSize = fontSize
            )

            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = fontSize.times(0.8f)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "$number",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.times(1.3f),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(10f)
        )
    }
}

@Composable
private fun StatLabel(title: String, subtitle: String, number: Number, indicator: Color){
    StatLabel(title = title, subtitle = subtitle, number = number) {
        Box(modifier = Modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(indicator)
        )
    }
}

@Composable
private fun FluencyIndexChart(analysisResults: AnalysisResults){
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
        title = "Fluidez"
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fi = "indice de fluidez"
            val indexExplanation = buildAnnotatedString {
                append("El ")
                withStyle(style = SpanStyle(color = Color.Black) ) {
                    pushStringAnnotation(tag = fi, annotation = fi)
                    append(fi)
                }
                append(" representa el porcentaje de palabras que fueron pronunciadas de manera correcta con respecto a la cantidad de palabras totales habladas por la persona. Un indice de fluidez alto significa que la persona pudo comunicarse exitosamente durante gran parte de la sesion.")
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
                    title = "palabras totales",
                    subtitle = "palabras habladas durante la sesion",
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
                    title = "palabras limpias",
                    subtitle = "pronunciadas de manera correcta",
                    number = analysisResults.cleanWordsCount,
                    indicator = colors[0]
                )

                StatLabel(
                    title = "disfluencias",
                    subtitle = "errores en la comunicacion",
                    number = analysisResults.totalDisfluencies,
                    indicator = colors[1]
                )
            }
        }
    }
}

//https://dribbble.com/shots/13960806-Business-app

/**
 * Visualizacion de Resultados:
 *
 *  totalWords, totalDisfluencies, cleanWordsCount y fluencyIndex
 *      pueden ir juntas en un grafico de torta, que ponga en un color las clean
 *      y en otro las disfluencias, que en el medio tenga el indice de fluidez
 *      y al costado diga los numeros de paalabras totales, disfluencias y limpias
 *
 *   totalPhrases, avgDisfluenciesPerPhrase
 *      numeros grandes con su titulo que muestren ambos datos uno al lado del otro
 *
 *   disfluencyStats
 *      mostrar grafico de barra con el count de cada una (con su color cada una)
 *      mostrar grafico de torta con el porcentaje de cada una, y al lado que diga el count total
 *
 *      tambien se podria mostrar para cada una (una abajo de la otra)
 *      un panel que diga el nombre completo de la disfluencia, una explicacion breve de
 *      en que consiste, y los 3 datos del DisfluencyStat
 *
 */