package com.disfluency.components.icon

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconLabeled(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = Color.Black
){
    Row(modifier) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = iconColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = labelColor,
            modifier = Modifier
                .padding(start = 4.dp)
                .height(20.dp)
                .wrapContentHeight()
        )
    }
}