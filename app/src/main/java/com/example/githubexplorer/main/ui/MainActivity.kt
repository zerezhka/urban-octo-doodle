package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.ImageLoader
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            val navController = rememberNavController()

            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(navController = navController, startDestination = NavigationC.UserFinder, modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        composable<NavigationC.UserFinder> {
                            UserScreen(
                                placeHolder = null,
                                imageLoader = imageLoader,
                                onNavigate = { user ->
                                    Timber.d("User clicked: $user")
                                    navController.navigate("${NavigationC.ReposList.route}/${user.name}/${user.avatar.replace("/", "%2F")}")
                                },
                            )
                        }
                        composable(
                            route = "${NavigationC.ReposList.route}/{name}/{avatar}",
                            arguments = listOf(
                                navArgument("name") { type = NavType.StringType },
                                navArgument("avatar") { type = NavType.StringType },
                            ),
                        ) { backStackEntry ->

                            Timber.d("$$$$$$$$$$$$$$$$$$$$$$$$")
                            Timber.d(backStackEntry.toString())

                            Timber.d("$$$$$$$$$$$$$$$$$$$$$$$$")
                            val arguments = requireNotNull(backStackEntry.arguments)
                            val name = arguments.getString("name")
                            val avatar = arguments.getString("avatar")?.replace("%2F","\\/", )

                            Text("ReposList for $name not implemented yet\n $avatar")
//                        Text("ReposList not implemented yet")
                        }
                        composable<NavigationC.DownloadScreen> { Text("DownloadScreen not implemented yet") }
                        // Add more destinations similarly.
                    }
                }
            }
        }
    }
}