package com.disfluency.components.audio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


const val MAX_SPIKES = 30

@Composable
fun AudioLiveWaveform(amplitudes: MutableList<Float>, maxSpikes: Int = MAX_SPIKES, maxHeight: Dp){
    val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

    //TODO: ver implementacion con Visualizer
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp)
        .drawBehind {
            try {
                val iterate = amplitudes.toList().takeLast(maxSpikes).listIterator()
                while (iterate.hasNext()){
                    val index = iterate.nextIndex()
                    val amp = iterate.next()

                    val x = index * (size.width / maxSpikes)
                    val length = maxHeight.value * amp
                    val y = size.center.y - length/2

                    if (amp > 0)
                        drawLine(
                            color = color,
                            start = Offset(x = x, y = y),
                            end = Offset(x = x, y = y + length),
                            strokeWidth = 15f,
                            cap = StrokeCap.Round
                        )
                }
            } catch (_: Exception) {
            }
        }
    )
}