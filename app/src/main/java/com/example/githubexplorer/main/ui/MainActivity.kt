package com.example.githubexplorer.main.ui

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.ImageLoader
import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.downloads.ui.DownloadOverlayViewModel
import com.example.githubexplorer.downloads.ui.DownloadProgressOverlay
import com.example.githubexplorer.downloads.ui.DownloadsScreen
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import com.ketch.Ketch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var fileDownloader: Ketch

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val overlayViewModel = hiltViewModel<DownloadOverlayViewModel>()
            val activeDownloads by overlayViewModel.activeDownloads.collectAsState()

            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = NavigationC.UserFinder,
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() },
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable<NavigationC.UserFinder> {
                                CreateSearchScreen(navController, imageLoader)
                            }
                            composable<NavigationC.ReposList> { backStackEntry ->
                                val route = backStackEntry.toRoute<NavigationC.ReposList>()

                                CreateReposScreen(
                                    name = route.name, avatar = route.avatar, imageLoader = imageLoader,
                                    onDownloadClick = { repo ->
                                        fileDownloader.download(
                                            url = "https://api.github.com/repos/${repo.owner}/${repo.name}/zipball",
                                            headers = hashMapOf<String, String>(
                                                "Authorization" to BuildConfig.GITHUB_TOKEN,
                                                "Accept" to "application/vnd.github.v3+json",
                                                "X-GitHub-Api-Version" to "2022-11-28"),
                                            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
                                            fileName = repo.name,
                                            tag = repo.url,
                                        )
                                    },
                                )
                            }
                            composable<NavigationC.DownloadScreen> {
                                DownloadsScreen()
                            }
                        }

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
