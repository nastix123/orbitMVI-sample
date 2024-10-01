package com.example.orbitmvi_sample.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.orbitmvi_sample.presentation.AppViewModel
import com.example.orbitmvi_sample.presentation.details.PhotoDetailScreen
import com.example.orbitmvi_sample.presentation.details.PhotoDetailViewModel
import com.example.orbitmvi_sample.presentation.list.ImagesScreen

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: AppViewModel, viewModelDetail: PhotoDetailViewModel) {
    NavHost(navController = navController, startDestination = "images_screen_route") {

        // Главный экран с фотографиями
        composable("images_screen_route") {

            ImagesScreen(viewModel = viewModel, navController = navController)
        }

        // Экран с деталями фотографии
        composable(
            route = "photo_detail_screen_route/{photoId}",
            arguments = listOf(navArgument("photoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val photoId = backStackEntry.arguments?.getInt("photoId")
            PhotoDetailScreen(
                navController = navController, photoId = photoId ?: -1, viewModel = viewModelDetail)
        }
    }
}
