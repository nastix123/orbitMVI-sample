package com.example.orbitmvi_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.orbitmvi_sample.navigation.NavigationGraph
import com.example.orbitmvi_sample.presentation.AppViewModel
import com.example.orbitmvi_sample.presentation.details.PhotoDetailViewModel
import com.example.orbitmvi_sample.presentation.list.ImagesScreen
import com.example.orbitmvi_sample.ui.theme.OrbitMVIsampleTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            OrbitMVIsampleTheme {
                val viewModel: AppViewModel by viewModel()
                val viewModelDetail: PhotoDetailViewModel by viewModel()
               NavigationGraph(navController = navController, viewModel = viewModel, viewModelDetail = viewModelDetail)
            }
        }
    }
}

