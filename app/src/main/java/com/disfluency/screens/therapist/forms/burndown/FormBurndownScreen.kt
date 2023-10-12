package com.disfluency.screens.therapist.forms.burndown

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.disfluency.R
import com.disfluency.components.charts.rememberChartStyle
import com.disfluency.components.charts.rememberMarker
import com.disfluency.components.icon.ImageMessagePage
import com.disfluency.model.form.FormQuestion
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.lighten
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollState
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun FormBurnDownScreen(
    reports: List<FormQuestionReport>
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        reports.forEachIndexed { index, it ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(Color.White)
            ) {
                FormQuestionResponseBurnDown(
                    formQuestion = it.question,
                    questionNumber = index,
                    data = it.scoresByAssignmentDate,
                    color = it.chartColor
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

    //TODO: ver si se puede agregar un onclick a los markers
    // y que al tocar te muestre la follow up en un globito

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
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 28.dp,
                        top = 8.dp
                    ),
                data = data,
                scrollState = scrollState,
                color = color.lighten(0.7f)
            )

            ChartScaleText(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(bottom = 175.dp),
                text = formQuestion.maxValue,
                color = color.lighten(0.2f)
            )

            ChartScaleText(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(top = 145.dp),
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
private fun ChartScaleText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color
){
    Text(
        text = text,
        color = color,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 11.sp,
        modifier = modifier.width(40.dp)
    )
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
                guideline = null,
                label = textComponent(padding = dimensionsOf(end = 32.dp))
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

@Composable
fun OnlyOneResponseMessageScreen(){
    ImageMessagePage(
        imageResource = R.drawable.chart,
        text = stringResource(id = R.string.form_has_only_one_response),
        fontSize = 15.sp,
        lineHeight = 15.sp,
        modifier = Modifier.width(300.dp)
    )
}