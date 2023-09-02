package com.disfluency.navigation.structure

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNavigationScaffold(
    title: String,
    navController: NavHostController,
    onBackNavigation: () -> Unit = { navController.popBackStack() },
    actions: @Composable() (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackNavigation) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        content = content
    )
}