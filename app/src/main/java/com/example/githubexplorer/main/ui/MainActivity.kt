package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val stateIsLoading = viewModel.isLoading
        val clicker = {
            stateIsLoading.value = !stateIsLoading.value
        }

        viewModel.viewModelScope.launch{
            withContext(Dispatchers.IO) {
                viewModel.search("zerezhka")// initial search
            }
        }
        setContent {
            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoading = remember { stateIsLoading }
                    val searchResult = remember { viewModel.searchResult.value }
                    if (!isLoading.value)
                        UserScreen(
                            searchResult,
                            clicker, null, innerPadding,
                            imageLoader = imageLoader,
                        )
                    else
                        LoadingScreen(clicker)
                }
            }
        }
    }
}