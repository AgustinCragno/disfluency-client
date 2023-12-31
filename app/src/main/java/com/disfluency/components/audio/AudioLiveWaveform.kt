package com.disfluency.components.audio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.disfluency.audio.record.MAX_AMPLITUDE_VALUE
import com.disfluency.components.audio.values.*


const val MAX_SPIKES = 37


@Composable
fun AudioLiveWaveform(
    amplitudes: List<Float>,
    maxSpikes: Int = MAX_SPIKES,
    spikeWidth: Dp = WAVEFORM_SPIKE_WIDTH.dp,
    spikeRadius: Dp = WAVEFORM_SPIKE_RADIUS.dp,
    spikePadding: Dp = WAVEFORM_SPIKE_PADDING.dp,
    spikeHeight: Dp = WAVEFORM_SPIKE_HEIGHT.dp,
    color: Color = MaterialTheme.colorScheme.primary
){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(spikeHeight)
        .drawBehind {
            try {
                val iterate = amplitudes.toList().takeLast(maxSpikes).listIterator()
                while (iterate.hasNext()){
                    val index = iterate.nextIndex()
                    val amplitude = iterate.next()

                    val x = index * (spikeWidth.toPx() + spikePadding.toPx())

                    val length = calculateLength(amplitude, spikeHeight).toPx()

                    val y = size.center.y - length/2

                    drawRoundRect(
                        brush = SolidColor(color),
                        topLeft = Offset(
                            x = x,
                            y = y
                        ),
                        size = Size(
                            width = spikeWidth.toPx(),
                            height = length
                        ),
                        cornerRadius = CornerRadius(spikeRadius.toPx(), spikeRadius.toPx()),
                        style = Fill
                    )
                }
            } catch (_: Exception) {
            }
        }
    )
}

private fun calculateLength(amplitude: Float, maxSpikeHeight: Dp): Dp {
    val amplitudeLength = maxSpikeHeight.value * (amplitude / MAX_AMPLITUDE_VALUE)
    return amplitudeLength.coerceIn(WAVEFORM_SPIKE_MIN_DRAWABLE_HEIGHT, maxSpikeHeight.value).dp
}