package com.example.dvillicaamusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.dvillicaamusicapp.navigation.AppNavGraph
import com.example.dvillicaamusicapp.ui.theme.DVillicañaMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DVillicañaMusicAppTheme {
                AppNavGraph()
            }
        }
    }
}