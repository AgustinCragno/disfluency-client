package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.disfluency.R
import com.disfluency.components.charts.BarChart
import com.disfluency.components.charts.DonutChart
import com.disfluency.model.analysis.AnalysisResults
import com.disfluency.model.analysis.DisfluencyType
import com.disfluency.model.analysis.DisfluencyTypeStats
import kotlin.math.ceil


@Composable
fun DisfluencyTypeCharts(analysisResults: AnalysisResults){
    val barMaxWidth = 170.dp

    val disfluencyTypeCountList = analysisResults.disfluencyStats
        .map { it.count.toFloat() }

    val disfluencyTypeColorList = analysisResults.disfluencyStats
        .map(DisfluencyTypeStats::type)
        .map(DisfluencyType::color)

    val disfluencyTypeNameList = analysisResults.disfluencyStats
        .map { it.type.fullName }


    StatsPanel(title = stringResource(R.string.disfluencies)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BarChart(
                modifier = Modifier
                    .padding(6.dp)
                    .height(500.dp),
                values = disfluencyTypeCountList,
                colors = disfluencyTypeColorList,
                labels = disfluencyTypeNameList,
                maxWidth = barMaxWidth
            )

            StatLabel(
                title = stringResource(R.string.total),
                subtitle = stringResource(R.string.disfluencies_produced_by_patient),
                number = analysisResults.totalDisfluencies
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Outlined.BorderColor,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Divider()

            DisfluencyTypeDonutChart(analysisResults = analysisResults)

            Divider()

            DisfluencyTypeDetailPanel(analysisResults = analysisResults)
        }
    }
}

@Composable
private fun DisfluencyTypeDonutChart(analysisResults: AnalysisResults){
    val chartValues = analysisResults.disfluencyStats.map { it.count.toFloat() }
    val colors = analysisResults.disfluencyStats.map { it.type.color }
    val labels = analysisResults.disfluencyStats
        .map { it.type.name }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ){
        DonutChart(
            modifier = Modifier.size(120.dp),
            sliceWidthDp = 20.dp,
            colors = colors,
            inputValues = chartValues,
            labels = labels,
            centerText = "${analysisResults.totalDisfluencies}  ",
            textColor = Color.Black
        )
    }
}

@Composable
private fun DisfluencyTypeDetailPanel(analysisResults: AnalysisResults){
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .drawWithCache {
            onDrawBehind {
                drawLine(
                    start = Offset(center.x, 0f),
                    end = Offset(center.x, size.height),
                    color = Color.LightGray,
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ){
            val columns = 2
            val size = analysisResults.disfluencyStats.size
            val length = ceil(size.toFloat() / columns.toFloat()).toInt()

            (0 until columns).forEach { index ->
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    val start = index * length
                    val end = (start + length).coerceAtMost(size)

                    analysisResults.disfluencyStats.subList(start, end).forEachIndexed { i, it ->
                        DisfluencyTypeInfo(
                            disfluencyTypeStats = it,
                            analysisResults = analysisResults,
                            trailingContent = { if (i < end - 1) Divider() }
                        )
                    }
                }
            }
        }
    }
}
