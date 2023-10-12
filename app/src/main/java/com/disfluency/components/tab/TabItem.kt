package com.disfluency.components.tab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(
    val title: String,
    val iconOn: ImageVector,
    val iconOff: ImageVector,
    val numberBadge: Int? = null,
    val content: @Composable () -> Unit
)