package com.disfluency.navigation.structure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.disfluency.navigation.routing.BottomNavigationItem
import com.disfluency.navigation.routing.NO_BOTTOM_BAR_ROUTES
import com.disfluency.navigation.routing.NO_TOP_BAR_ROUTES
import com.disfluency.navigation.routing.getTitleByRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(bottomNavigationItems: List<BottomNavigationItem>, content: @Composable (NavHostController)->Unit) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?: bottomNavigationItems[0].route.path

    Scaffold(
        topBar = {
            if (!NO_TOP_BAR_ROUTES.contains(currentRoute)){
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(getTitleByRoute(currentRoute))) }
                )
            }
        },
        bottomBar = {
            if (!NO_BOTTOM_BAR_ROUTES.contains(currentRoute)){
                BottomNavigation(navController, bottomNavigationItems)
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                content(navController)
            }
        }
    )
}

@Composable
fun BottomNavigation(navController: NavController, items: List<BottomNavigationItem>) {

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route.path,
                icon = { Icon(item.icon, contentDescription = stringResource(item.route.title)) },
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