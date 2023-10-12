package com.disfluency.components.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextTag(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    offsetY: Dp = 3.dp
){
    Box(
        modifier = modifier
            .wrapContentWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 11.sp,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp)
                .offset(y = offsetY)
        )
    }
}