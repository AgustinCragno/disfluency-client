package com.disfluency.screens.therapist.forms.burndown

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.ViewTimeline
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.disfluency.components.charts.rememberChartStyle
import com.disfluency.components.charts.rememberMarker
import com.disfluency.components.icon.TextTag
import com.disfluency.components.tab.TabItem
import com.disfluency.components.tab.TabScreen
import com.disfluency.model.form.FormCompletionEntry
import com.disfluency.model.form.FormQuestion
import com.disfluency.navigation.structure.BackNavigationScaffold
import com.disfluency.screens.therapist.forms.burndown.FormQuestionReport
import com.disfluency.ui.theme.DisfluencyTheme
import com.disfluency.utilities.color.darken
import com.disfluency.utilities.color.lighten
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
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


@Composable
fun FormBurnDownScreen(
    reports: List<FormQuestionReport>
){
    val colors = listOf(Color.Blue, Color.Magenta, Color.Red, Color.Green.darken(), Color.Cyan.darken(0.5f))

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
//
//    val selectedItem = remember {
//        mutableStateOf<Marker.EntryModel?>(null)
//    }

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
            modifier = modifier
//                .pointerInput(Unit) {
//                    detectTapGestures { offset ->
//                        val indicatorSize = INDICATOR_SIZE_DP
//                        val visiblePoints = lineChart.entryLocationMap.values.flatten()
//
//                        val intersection = visiblePoints.firstOrNull {
//                            val p = it.location
//                            (offset.x > p.x - indicatorSize / 2) && (offset.x < p.x + indicatorSize / 2) &&
//                                    (offset.y > p.y - indicatorSize / 2) && (offset.y < p.y + indicatorSize / 2)
//                        }
//
//                        selectedItem.value = intersection
//                    }
//                }
//                .drawWithContent {
//                    drawContent()
//
//                    selectedItem.value?.let {
//                        val height = 300f
//                        val width = 500f
//
//                        drawRect(
//                            color = Color.Green,
//                            topLeft = Offset(it.location.x - width / 2, it.location.y - height),
//                            size = Size(width, height)
//                        )
//                    }
//                }
            ,
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

//    selectedItem.value?.let {
//        Surface(
//            modifier = Modifier.size(50.dp)
//                .background(Color.Green)
//                .offset(x = it.location.x.dp, y = it.location.y.dp),
//            color = Color.Green,
//            shape = RoundedCornerShape(8.dp)
//        ){}
//    }

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