package com.example.orbitmvi_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.orbitmvi_sample.di.appModule
import com.example.orbitmvi_sample.presentation.AppViewModel
import com.example.orbitmvi_sample.presentation.list.ImagesScreen
import com.example.orbitmvi_sample.ui.theme.OrbitMVIsampleTheme
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OrbitMVIsampleTheme {
                val viewModel: AppViewModel by viewModel()
                ImagesScreen(viewModel = viewModel)
            }
        }
    }
}

