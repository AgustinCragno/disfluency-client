package com.disfluency.screens.therapist.analysis.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun StatLabel(title: String, subtitle: String, number: Number, indicator: Color){
    StatLabel(title = title, subtitle = subtitle, number = number) {
        Box(modifier = Modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(indicator)
        )
    }
}

@Composable
fun StatLabel(title: String, subtitle: String, number: Number, leadingContent: @Composable () -> Unit){
    val fontSize = 15.sp

    Divider()

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent()

        Spacer(modifier = Modifier.width(12.dp))

        Column() {
            Text(
                text = title,
                color = Color.Black,
                fontSize = fontSize
            )

            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = fontSize.times(0.8f)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "$number",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.times(1.3f),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(10f)
        )
    }
}