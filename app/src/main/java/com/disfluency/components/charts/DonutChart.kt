package com.disfluency.components.charts

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * https://betterprogramming.pub/custon-charts-in-android-using-jetpack-compose-87b395c1d515
 */


/**
 * Component for creating Donut Chart
 * Slices are painted clockwise
 * e.g. 1st input value starts from top to the right, etc
 */

private const val animationDuration = 800
private const val chartDegrees = 360f
private const val emptyIndex = -1
private val defaultSliceWidth = 20.dp
private val defaultSlicePadding = 5.dp
private val defaultSliceClickPadding = 10.dp

@Composable
internal fun DonutChart(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    inputValues: List<Float>,
    textColor: Color = MaterialTheme.colors.primary,
    centerText: String = "",
    centerTextSize: Dp = 30.dp,
    sliceWidthDp: Dp = defaultSliceWidth,
    slicePaddingDp: Dp = defaultSlicePadding,
    sliceClickPaddingDp: Dp = defaultSliceClickPadding,
    animated: Boolean = true
) {

    assert(inputValues.isNotEmpty() && inputValues.size == colors.size) {
        "Input values count must be equal to colors size"
    }

    // calculate each input percentage
    val proportions = inputValues.toPercent()

    // calculate each input slice degrees
    val angleProgress = proportions.map { prop ->
        chartDegrees * prop / 100
    }

    // start drawing clockwise (top to right)
    var startAngle = 270f

    // used for animating each slice
    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }

    // calculate each slice end point in degrees, for handling click position
    val progressSize = mutableListOf<Float>()

    LaunchedEffect(angleProgress) {
        progressSize.add(angleProgress.first())
        for (x in 1 until angleProgress.size) {
            progressSize.add(angleProgress[x] + progressSize[x - 1])
        }
    }

    val density = LocalDensity.current

    //convert dp values to pixels
    val sliceWidthPx = with(density) { sliceWidthDp.toPx() }
    val slicePaddingPx = with(density) { slicePaddingDp.toPx() }
    val sliceClickPaddingPx = with(density) { sliceClickPaddingDp.toPx() }

    // text style
    val textFontSize = with(density) { centerTextSize.toPx() }
    val textPaint = remember {
        Paint().apply {
            color = textColor.toArgb()
            textSize = textFontSize
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
    }

    // animate chart slices on composition
    LaunchedEffect(inputValues) {
        pathPortion.animateTo(1f, animationSpec = tween(if (animated) animationDuration else 0))
    }

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {

        val canvasSize = min(constraints.maxWidth, constraints.maxHeight)
        val padding = canvasSize * slicePaddingPx / 100f
        val size = Size(canvasSize.toFloat() - padding, canvasSize.toFloat() - padding)
        val canvasSizeDp = with(density) { canvasSize.toDp() }

        Canvas(
            modifier = Modifier.size(canvasSizeDp)
        ) {

            angleProgress.forEachIndexed { index, angle ->
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = angle * pathPortion.value,
                    useCenter = false,
                    size = size,
                    style = Stroke(width = sliceWidthPx),
                    topLeft = Offset(padding / 2, padding / 2)
                )
                startAngle += angle
            }

            drawIntoCanvas { canvas ->

                canvas.nativeCanvas.drawText(
                    centerText,
                    (canvasSize / 2) + textFontSize / 4,
                    (canvasSize / 2) + textFontSize / 4,
                    textPaint
                )
            }
        }
    }

}

internal fun List<Float>.toPercent(): List<Float> {
    return this.map { item ->
        item * 100 / this.sum()
    }
}
