package com.disfluency.components.scroll

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp

fun Modifier.verticalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color? = null,
) = composed(
    debugInspectorInfo {
        name = "length"
        value = length
    }
) {
    val color = edgeColor ?: MaterialTheme.colorScheme.surface

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromTop by derivedStateOf { scrollState.value }
        val scrollFromBottom by derivedStateOf { scrollState.maxValue - scrollState.value }

        val topFadingEdgeStrength = lengthValue * (scrollFromTop / lengthValue).coerceAtMost(1f)

        val bottomFadingEdgeStrength = lengthValue * (scrollFromBottom / lengthValue).coerceAtMost(1f)

        drawContent()

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startY = 0f,
                endY = topFadingEdgeStrength,
            ),
            size = Size(
                this.size.width,
                topFadingEdgeStrength
            ),
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startY = size.height - bottomFadingEdgeStrength,
                endY = size.height,
            ),
            topLeft = Offset(x = 0f, y = size.height - bottomFadingEdgeStrength),
        )
    }
}