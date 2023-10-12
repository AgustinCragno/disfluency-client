package com.disfluency.components.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImageMessagePage(
    modifier: Modifier = Modifier.width(160.dp),
    imageResource: Int,
    imageSize: Dp = 120.dp,
    text: String,
    fontSize: TextUnit = 15.sp,
    lineHeight: TextUnit = TextUnit.Unspecified
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier.size(imageSize),
                painter = painterResource(id = imageResource),
                contentDescription = "Speech",
                alpha = 0.4f
            )

            Text(
                modifier = modifier.padding(16.dp),
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize,
                lineHeight = lineHeight,
                textAlign = TextAlign.Center,
                color = Color.Gray.copy(alpha = 0.7f)
            )
        }
    }

}