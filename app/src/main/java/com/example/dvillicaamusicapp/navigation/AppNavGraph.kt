package com.example.dvillicaamusicapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.dvillicaamusicapp.ui.screens.DetailScreen
import com.example.dvillicaamusicapp.ui.screens.HomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onAlbumClick = { albumId ->
                    navController.navigate(Detail(albumId = albumId))
                }
            )
        }
        composable<Detail> { backStackEntry ->
            val detail: Detail = backStackEntry.toRoute()
            DetailScreen(
                albumId = detail.albumId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
