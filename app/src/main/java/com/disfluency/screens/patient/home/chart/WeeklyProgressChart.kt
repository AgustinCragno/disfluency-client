package com.disfluency.screens.patient.home.chart

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.disfluency.components.charts.rememberChartStyle
import com.disfluency.components.charts.rememberMarkerLabeled
import com.disfluency.utilities.color.lighten
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
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
fun WeeklyProgressChart(weeklyProgress: Map<LocalDate, Int>){
    val dataByIndex = (0 until weeklyProgress.size).zip(weeklyProgress.values)
    val chartEntryModel = entryModelOf(dataByIndex.map { entryOf(it.first.toFloat(), it.second) })

    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM")

    val marker = rememberMarkerLabeled(
        labelColor = Color.Red,
        labelBackgroundColor = Color.Red.lighten().copy(alpha = 0.2f)
    )

    val maxValueRoundedToMultipleOfTen = round(weeklyProgress.values.max())

    val axisValueOverrider = AxisValuesOverrider.fixed(minY = 0f, maxY = maxValueRoundedToMultipleOfTen.toFloat())

    val scrollState = rememberChartScrollState()

    ProvideChartStyle(rememberChartStyle(emptyList(), listOf(Color.Magenta.lighten()))) {
        val defaultLines = currentChartStyle.lineChart.lines

        val lineChart = lineChart(
            lines = remember(defaultLines) {
                defaultLines.map { defaultLine -> defaultLine.copy(pointConnector = DefaultPointConnector(cubicStrength = 0f)) }
            },
            persistentMarkers = remember(marker) { dataByIndex.map { it.first.toFloat() }.associateWith { marker } },
            axisValuesOverrider = axisValueOverrider,
            spacing = 65.dp
        )

        Chart(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                    top = 28.dp
                ),
            chart = lineChart,
            model = chartEntryModel,
            startAxis = startAxis(
                maxLabelCount = calculateNumberOfSteps(maxChartValue = maxValueRoundedToMultipleOfTen),
                guideline = null
            ),
            bottomAxis = bottomAxis(
                valueFormatter = { value, _ ->
                    val dateValue = weeklyProgress.keys.toList()[value.toInt()]
                    (dateValue.format(dateTimeFormatter))
                },
                guideline = null
            ),
            chartScrollState = scrollState
        )
    }
}

private fun calculateNumberOfSteps(minStepsAllowed: Int = 4, maxChartValue: Int): Int {
    var s = minStepsAllowed
    var steps = 0.1f

    while (steps.mod(1f) != 0f) {
        steps = maxChartValue.toFloat() / s.toFloat()
        s++
    }

    return s
}

/**
 * Rounds up to closest multiple of 10 (upper)
 */
private fun round(n: Int): Int {
    // Smaller multiple
    val a = n / 10 * 10

    // Larger multiple
    return a + 10
}