package com.example.calcapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.calcapp.ui.theme.CalcAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalcAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val viewModel: CalculatorViewModel = koinViewModel()
                    CalculatorScreen(viewModel = viewModel)
                }
            }
        }
    }
}


