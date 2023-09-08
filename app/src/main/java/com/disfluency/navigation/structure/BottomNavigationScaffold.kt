package com.disfluency.navigation.structure

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.disfluency.components.text.AdjustableSizeText
import com.disfluency.components.text.AdjustableSizeUnit
import com.disfluency.navigation.routing.BottomNavigationItem

@Composable
fun BottomNavigationScaffold(
    bottomNavigationItems: List<BottomNavigationItem>,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController, bottomNavigationItems)
        },
        content = content
    )
}

@Composable
private fun BottomNavigation(navController: NavController, items: List<BottomNavigationItem>) {

    val textSize = remember { AdjustableSizeUnit(12.sp) }

    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route.path,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                label = {
                    AdjustableSizeText(text = stringResource(item.title), adjustableSize = textSize)
                },
                onClick = {
                    navController.navigate(item.route.path) {

                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = false
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}