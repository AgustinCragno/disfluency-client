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
import kotlin.math.*

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
private val defaultSliceClickPadding = 2.dp

@Composable
internal fun DonutChart(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    inputValues: List<Float>,
    labels: List<String>,
    textColor: Color = MaterialTheme.colors.primary,
    centerText: String = "",
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

    var clickedItemIndex by remember {
        mutableStateOf(emptyIndex)
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

    val selectedSliceWidth = sliceWidthPx + sliceClickPaddingPx

    // text style
    val textFontSize = with(density) { 30.dp.toPx() }
    val textPaint = remember {
        Paint().apply {
            color = textColor.copy(alpha = 0.8f).toArgb()
            textSize = textFontSize
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
    }

    val labelFontSize = with(density) { 15.dp.toPx() }


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
                .pointerInput(inputValues) {

                detectTapGestures { offset ->
                    val clickedAngle = touchPointToAngle(
                        width = canvasSize.toFloat(),
                        height = canvasSize.toFloat(),
                        touchX = offset.x,
                        touchY = offset.y,
                        chartDegrees = chartDegrees
                    )
                    progressSize.forEachIndexed { index, item ->
                        if (clickedAngle <= item) {
                            clickedItemIndex = if (clickedItemIndex != index){
                                index
                            } else {
                                emptyIndex
                            }

                            return@detectTapGestures
                        }
                    }
                }
            }
        ) {

            angleProgress.forEachIndexed { index, angle ->
                val spacing = 1.2f

                val angleRad = Math.toRadians(startAngle.toDouble() - 180 + angle / 2)
                val h = (canvasSize / 2) * spacing
                var offsetX = (-cos(angleRad) * h).toFloat()
                var offsetY = (-sin(angleRad) * h).toFloat()

                if (offsetY > 0){
                    offsetX *= 1.1f
                    offsetY *= 1.1f
                }

                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = angle * pathPortion.value,
                    useCenter = false,
                    size = size,
                    style = Stroke(width = if (clickedItemIndex == index) selectedSliceWidth else sliceWidthPx),
//                    style = Stroke(width = sliceWidthPx),
                    topLeft = Offset(padding / 2, padding / 2)
                )
                startAngle += angle


                val center = (canvasSize / 2).toFloat()

                if (angle != 0f){
                    val labelTextPaint =
                        Paint().apply {
                            color = colors[index].toArgb()
                            textSize = labelFontSize
                            textAlign = Paint.Align.CENTER
                            isFakeBoldText = true
                        }

                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            labels[index],
                            center + offsetX,
                            center + offsetY,
                            labelTextPaint
                        )
                    }
                }
            }

            drawIntoCanvas { canvas ->
                var textDisplay = centerText
                var textPaintColor = textPaint

                if (clickedItemIndex != emptyIndex){
                    textDisplay = "${proportions[clickedItemIndex].roundToInt()}%"
                    textPaintColor = Paint().apply {
                        color = colors[clickedItemIndex].copy(alpha = 0.5f).toArgb()
                        textSize = textFontSize
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                }

                canvas.nativeCanvas.drawText(
                    textDisplay,
                    (canvasSize / 2) + textFontSize / 4,
                    (canvasSize / 2) + textFontSize / 4,
                    textPaintColor
                )
            }
        }
    }
}


internal fun touchPointToAngle(
    width: Float,
    height: Float,
    touchX: Float,
    touchY: Float,
    chartDegrees: Float
): Double {
    val x = touchX - (width * 0.5f)
    val y = touchY - (height * 0.5f)
    var angle = Math.toDegrees(atan2(y.toDouble(), x.toDouble()) + Math.PI / 2)
    angle = if (angle < 0) angle + chartDegrees else angle
    return angle
}