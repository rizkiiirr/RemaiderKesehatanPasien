package com.example.remainderkesehatanpasien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.remainderkesehatanpasien.component.NavigationApp
import com.example.remainderkesehatanpasien.ui.theme.RemainderKesehatanPasienTheme
import dagger.hilt.android.AndroidEntryPoint // <-- TAMBAHKAN IMPORT INI

@AndroidEntryPoint // <-- TAMBAHKAN ANOTASI INI
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var darkMode by rememberSaveable { mutableStateOf(false) }
            RemainderKesehatanPasienTheme(darkTheme = darkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavigationApp(
                        navController = navController,
                        darkMode = darkMode,
                        onToggleDarkMode = { darkMode = !darkMode }
                    )
                }
            }
        }
    }
}