package com.disfluency.components.icon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IconLabeled(
    icon: ImageVector,
    label: String,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = Color.Black,
    labelSize: TextUnit = 12.sp
){
    Row {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontSize = labelSize,
            color = labelColor,
            modifier = Modifier
                .padding(start = 4.dp)
                .height(20.dp)
                .wrapContentHeight()
        )
    }
}