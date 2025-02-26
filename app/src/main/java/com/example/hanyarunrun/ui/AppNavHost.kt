package com.example.hanyarunrun.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.hanyarunrun.viewmodel.DataViewModel
import com.example.hanyarunrun.ui.screens.SplashScreen

@Composable
fun AppNavHost(viewModel: DataViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // BottomBar hanya muncul di halaman list, form, dan profile
            if (currentRoute in listOf("list", "form", "profile")) {
                BottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("form") {
                DataEntryScreen(navController = navController, viewModel = viewModel)
            }
            composable("list") {
                DataListScreen(navController = navController, viewModel = viewModel)
            }
            composable("profile") {
                ProfileScreen(modifier = Modifier)
            }
            composable(
                route = "edit/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                EditScreen(navController = navController, viewModel = viewModel, dataId = id)
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        val navigationItems = listOf(
            NavigationItem(
                title = "List",
                icon = Icons.Default.List,
                route = "list"
            ),
            NavigationItem(
                title = "Form",
                icon = Icons.Default.AddCircle,
                route = "form"
            ),
            NavigationItem(
                title = "Profile",
                icon = Icons.Default.AccountCircle,
                route = "profile"
            )
        )

        navigationItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
