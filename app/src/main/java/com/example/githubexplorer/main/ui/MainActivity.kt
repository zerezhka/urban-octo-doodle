package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
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


        setContent {
            val navController = rememberNavController()

            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(navController = navController, startDestination = NavigationC.UserFinder, modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        composable<NavigationC.UserFinder> {
                            val searchResult = viewModel.searchResult.collectAsState()
                            val query = viewModel.query.collectAsState()
                            UserScreen(searchResult.value,
                                queryText = query.value,
                                onQueryChange = { viewModel.query.value = it },
                                placeHolder = null,
                                imageLoader = imageLoader,
                                search = {
                                    viewModel.search(query.value)
                                },
                                onUserClick = { user ->
                                    navController.navigate(NavigationC.ReposList(user.name))
                                },
                            )
                        }
                        composable<NavigationC.ReposList>(
                            ) { backStackEntry->
//                            val profile: GithubUser = backStackEntry.toRoute()
//                            Text("ReposList of $profile not impemented yet")
                            Text("ReposList not implemented yet")
                        }
                        composable<NavigationC.DownloadScreen> { Text("DownloadScreen not implemented yet") }
                        // Add more destinations similarly.
                    }


//                        UserScreen(
//                            users = emptyList(),
//                            queryText = "",
//                            search = {},
//                            onQueryChange = {},
//                            placeHolder = null,
//                            imageLoader = imageLoader,
//                            onUserClick = {
//                                navController.navigate(
//                                    route = Navigation.ReposList(it)
//                                )
//                            }
//                        )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainActivity() {
    GithubExplorerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(navController = rememberNavController(), startDestination = NavigationC.UserFinder) {
                    composable<NavigationC.UserFinder> {
                        Text("UserFinder not implemented yet")
                    }
                    composable<NavigationC.ReposList> {
                        Text("ReposList not implemented yet")
                    }
                    composable<NavigationC.DownloadScreen> {
                        Text("DownloadScreen not implemented yet")
                    }
                }
                UserScreen(
                    users = emptyList(),
                    queryText = "",
                    search = {},
                    onQueryChange = {},
                    placeHolder = null,
                    imageLoader = null,
                    onUserClick = {
                        //navController.navigate(Navigation.ReposList(user))
                    }
                )
            }
        }
    }
}