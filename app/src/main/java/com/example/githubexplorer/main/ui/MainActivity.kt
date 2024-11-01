package com.example.githubexplorer.main.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.ImageLoader
import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.NavigationC
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

            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavigationC.UserFinder,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable<NavigationC.UserFinder> {
                            CreateSearchScreen(navController, imageLoader)
                        }
                        composable(
                            route = "${NavigationC.ReposList.ROUTE}/{name}/{avatar}",
                            arguments = listOf(
                                navArgument("name") { type = NavType.StringType },
                                navArgument("avatar") { type = NavType.StringType },
                            ),
                        ) { backStackEntry ->
                            val arguments = requireNotNull(backStackEntry.arguments)
                            val name = arguments.getString("name")
                            val avatar = arguments.getString("avatar")?.replace("%2F", "\\/")

                            val context = LocalContext.current
                            CreateReposScreen(
                                name = name!!, avatar = avatar, imageLoader = imageLoader,
                                onDownloadClick = { repo ->
                                    fileDownloader.download(
                                        // it responds with a new url to download the file, todo move everything to the viewmodel and retrofit @Streaming api
                                        // todo add a progress bar
                                        // NOT WORKING
                                        url = "https://api.github.com/repos/${repo.owner}/${repo.name}/zipball",
                                        headers = hashMapOf<String, String>(
                                            "Authorization" to BuildConfig.GITHUB_TOKEN,
                                            "Accept" to "application/vnd.github.v3+json",
                                            "X-GitHub-Api-Version" to "2022-11-28"),

                                        path = ".",
                                        fileName = repo.name,
                                        tag = repo.url,
                                    )

                                    Toast.makeText(context, "Downloading ${repo.name}", Toast.LENGTH_SHORT).show()
                                    // todo remove
                                    // draft open browser, just to have at least download functionality
                                     val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.github.com/repos/${repo.owner}/${repo.name}/zipball"))
                                    context.startActivity(intent)
                                    // todo remove
                                    fileDownloader.cancel(repo.url)
                                },
                            )
                        }
                        composable<NavigationC.DownloadScreen> {
                            DownloadsScreen()
                        }
                    }
                }
            }
        }
    }
}