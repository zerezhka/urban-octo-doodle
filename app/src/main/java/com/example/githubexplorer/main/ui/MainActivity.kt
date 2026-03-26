package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.githubexplorer.downloads.ui.DownloadOverlayViewModel
import com.example.githubexplorer.downloads.ui.DownloadProgressOverlay
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val overlayViewModel = hiltViewModel<DownloadOverlayViewModel>()
            val activeDownloads by overlayViewModel.activeDownloads.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                overlayViewModel.completedEvents.collect { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }

            GithubExplorerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AppNavGraph(
                            navController = navController,
                            modifier = Modifier.fillMaxSize()
                        )

                        DownloadProgressOverlay(
                            activeDownloads = activeDownloads,
                            onCancel = { overlayViewModel.cancel(it) },
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
