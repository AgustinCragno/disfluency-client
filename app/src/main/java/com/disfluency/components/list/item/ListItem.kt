package com.disfluency.components.list.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ListItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
){
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        ListItem(
            modifier = Modifier
                .height(56.dp)
                .clickable { onClick() },
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            leadingContent = leadingContent,
            trailingContent = trailingContent,
            colors = ListItemDefaults.colors(Color.Transparent)
        )
    }
}

@Composable
fun ListItem(
    title: String,
    supportingContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
){
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )
        },
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        onClick = onClick
    )
}

@Composable
fun ListItem(
    title: String,
    subtitle: String,
    subtitleColor: Color = Color.Gray,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    onClick: () -> Unit = {}
){
    ListItem(
        title = title,
        supportingContent = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = subtitleColor
            )
        },
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        onClick = onClick
    )
}