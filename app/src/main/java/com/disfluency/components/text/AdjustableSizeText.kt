package com.disfluency.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

class AdjustableSizeUnit(size: TextUnit){
    var textSize by mutableStateOf(size)
        private set

    fun scale(value: Float){
        textSize = textSize.times(value)
    }
}

@Composable
fun AdjustableSizeText(text: String, adjustableSize: AdjustableSizeUnit){
    Text(
        text = text,
        fontSize = adjustableSize.textSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

            if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                adjustableSize.scale(TEXT_SCALE_REDUCTION_INTERVAL)
            }
        }
    )
}