package com.disfluency.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val defaultMaxWidth = 200.dp
private val defaultLabelWidth = 120.dp


@Composable
internal fun BarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    colors: List<Color>,
    labels: List<String>,
    maxWidth: Dp = defaultMaxWidth
) {

    assert(values.isNotEmpty()) { "Input values are empty" }

    val maxValue = values.max()

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .drawBehind {
                    (0..5).forEach { i ->
                        val x = (i * (maxWidth.toPx() / 5)) + defaultLabelWidth.toPx()

                        drawLine(
                            color = Color.LightGray,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 2f,
                            alpha = 0.5f,
                            cap = StrokeCap.Round
                        )
                    }
                }
        ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        values.forEachIndexed { index, item ->
            Bar(
                value = item,
                color = colors[index],
                label = labels[index],
                maxValue = maxValue,
                maxWidth = maxWidth
            )
        }
    }

}

@Composable
private fun ColumnScope.Bar(
    value: Float,
    color: Color,
    label: String,
    maxValue: Float,
    maxWidth: Dp
) {
    val itemWidth = remember(value) { value * maxWidth.value / maxValue }

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 5.dp)
            .weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(defaultLabelWidth),
            textAlign = TextAlign.Start,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier
                .width(itemWidth.dp)
                .height(14.dp)
                .background(color)
        )

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = value.toInt().toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }

}