package com.disfluency.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * Text component that does not leave blank space when jumping to next line
 */
@Composable
fun WidthWrappedText(
    text: String,
    color: Color = Color.Black,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    padding: Dp = 0.dp
){
    var fullWidthPercentage by remember { mutableStateOf(1f) }
    var processed by remember(text) {
        mutableStateOf(false)
    }

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        lineHeight = fontSize.times(1.15f),
        color = color,
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth(fullWidthPercentage),
        onTextLayout = { result ->
            if (processed) return@Text

            val maxWidthLine = (0 until result.lineCount).maxOf { result.getLineRight(it) }
            val totalLayoutWidth = result.size.width.toFloat()

            fullWidthPercentage = maxWidthLine / totalLayoutWidth
            processed = true
        }
    )
}