package com.rzmmzdh.gita

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rzmmzdh.gita.core.theme.GitaTheme
import com.rzmmzdh.gita.feature_search.ui.navigation.Destination
import com.rzmmzdh.gita.feature_search.ui.search_repositories.SearchRepositoriesScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Entry : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            GitaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Destination.SearchRepositories.route
                    ) {
                        composable(Destination.SearchRepositories.route) {
                            SearchRepositoriesScreen(navController)
                        }
                    }
                    HideSystemUi()
                }
            }
        }
    }

    @Composable
    private fun HideSystemUi() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()

        DisposableEffect(systemUiController, useDarkIcons) {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
            onDispose {}
        }
    }
}