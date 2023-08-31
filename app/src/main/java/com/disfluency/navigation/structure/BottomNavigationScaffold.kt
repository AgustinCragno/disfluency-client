package com.disfluency.navigation.structure

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.NO_BOTTOM_BAR_ROUTES
import com.disfluency.navigation.routing.NO_TOP_BAR_ROUTES
import com.disfluency.navigation.routing.getTitleByRoute

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
                    //TODO: usar auto-adjustable-size text
                    Text(text = stringResource(item.route.title))
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