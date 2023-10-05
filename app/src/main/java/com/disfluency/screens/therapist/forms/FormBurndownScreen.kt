package com.disfluency.screens.therapist.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.components.charts.rememberChartStyle
import com.disfluency.components.charts.rememberMarker
import com.disfluency.model.form.FormQuestion
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.lighten
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollState
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Random

@Preview
@Composable
private fun FormBurnDownPreview(){
    DisfluencyTheme {
        FormBurnDownScreen()
    }
}

@Composable
private fun FormBurnDownScreen(){

    val question = FormQuestion(
        id = "",
        scaleQuestion = "Cuando tengo una duda en clase, levanto la mano y le pregunto al profesor",
        followUpQuestion = "",
        minValue = "Nunca",
        maxValue = "Siempre"
    )

    val data = listOf(
        "2022-09-03" to 2,
        "2022-10-03" to 2,
        "2022-10-10" to 1,
        "2022-10-17" to 4,
        "2022-10-24" to 3,
        "2022-10-31" to 3,
        "2022-11-07" to 4,
        "2022-11-14" to 5,
        "2022-11-21" to 3,
        "2022-11-28" to 5,
        "2022-12-10" to 2,
        "2022-12-17" to 4,
        "2022-12-24" to 3,
        "2022-12-31" to 3,
    ).associate { (dateString, yValue) ->
        LocalDate.parse(dateString) to yValue
    }

    val colors = listOf(Color.Blue, Color.Magenta, Color.Red, Color.Green.darken(), Color.Cyan.darken(0.5f))

    val list = (0..5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        list.forEach {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.Transparent)
            ) {
                FormQuestionResponseBurnDown(
                    formQuestion = question,
                    questionNumber = it,
                    data = data,
                    color = colors.random()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun FormQuestionResponseBurnDown(
    formQuestion: FormQuestion,
    questionNumber: Int,
    data: Map<LocalDate, Int>,
    color: Color = Color.Magenta
){
    val scrollState = rememberChartScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = formQuestion.scaleQuestion,
            style = MaterialTheme.typography.displayMedium,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp)
        ) {
            QuestionResponsesTimelyChart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
                data = data,
                scrollState = scrollState,
                color = color.lighten(0.7f)
            )

            TextTag(
                modifier = Modifier.align(Alignment.TopStart),
                text = formQuestion.maxValue,
                color = color.lighten(0.2f)
            )

            TextTag(
                modifier = Modifier.align(Alignment.BottomStart),
                text = formQuestion.minValue,
                color = color.darken(0.2f)
            )

            ScrollIndicatorArrow(
                modifier = Modifier.align(Alignment.BottomEnd),
                scrollState = scrollState
            )
        }
    }
}

@Composable
private fun QuestionResponsesTimelyChart(
    modifier: Modifier = Modifier,
    data: Map<LocalDate, Int>,
    scrollState: ChartScrollState,
    color: Color = Color(COLOR_4_CODE)
){
    val dataByIndex = (0 until data.size).zip(data.values)
    val chartEntryModel = entryModelOf(dataByIndex.map { entryOf(it.first.toFloat(), it.second) })

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")

    val marker = rememberMarker()

    ProvideChartStyle(rememberChartStyle(emptyList(), listOf(color))) {
        val defaultLines = currentChartStyle.lineChart.lines

        val lineChart = lineChart(
            lines = remember(defaultLines) {
                defaultLines.map { defaultLine -> defaultLine.copy(pointConnector = pointConnector) }
            },
            persistentMarkers = remember(marker) { dataByIndex.map { it.first.toFloat() }.associateWith { marker } },
            axisValuesOverrider = axisValueOverrider,
            spacing = 65.dp
        )

        Chart(
            modifier = modifier,
            chart = lineChart,
            model = chartEntryModel,
            startAxis = startAxis(
                maxLabelCount = 5,
                guideline = null
            ),
            bottomAxis = bottomAxis(
                valueFormatter = { value, _ ->
                    val dateValue = data.keys.toList()[value.toInt()]
                    (dateValue.format(dateTimeFormatter))
                },
                guideline = null
            ),
            chartScrollState = scrollState
        )
    }
}

private const val COLOR_4_CODE = 0xfffdc8c4

private val pointConnector = DefaultPointConnector(cubicStrength = 0f)
private val axisValueOverrider =
    AxisValuesOverrider.fixed(minY = 1f, maxY = 5f)


@Composable
private fun TextTag(
    modifier: Modifier = Modifier,
    text: String,
    color: Color
){
    Box(
        modifier = modifier
            .wrapContentWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
                .offset(y = 3.dp)
        )
    }
}

@Composable
private fun ScrollIndicatorArrow(
    modifier: Modifier = Modifier,
    scrollState: ChartScrollState
){
    if (scrollState.value < scrollState.maxValue - 32.dp.value){
        Icon(
            imageVector = Icons.Filled.ArrowRightAlt,
            contentDescription = null,
            tint = Color.Blue.copy(alpha = 0.1f),
            modifier = modifier
                .height(40.dp)
                .width(80.dp)
        )
    }
}