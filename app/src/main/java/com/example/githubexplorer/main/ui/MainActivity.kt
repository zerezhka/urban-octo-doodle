package com.example.githubexplorer.main.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.ImageLoader
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
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
                    NavHost(
                        navController = navController,
                        startDestination = NavigationC.UserFinder,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable<NavigationC.UserFinder> {
                            CreateSearchScreen(navController)
                        }
                        composable(
                            route = "${NavigationC.ReposList.route}/{name}/{avatar}",
                            arguments = listOf(
                                navArgument("name") { type = NavType.StringType },
                                navArgument("avatar") { type = NavType.StringType },
                            ),
                        ) { backStackEntry ->
                            val arguments = requireNotNull(backStackEntry.arguments)
                            val name = arguments.getString("name")
                            val avatar = arguments.getString("avatar")?.replace("%2F", "\\/")

                            CreateReposScreen(
                                name = name!!, avatar = avatar,
                            )
                        }
                        composable<NavigationC.DownloadScreen> { Text("DownloadScreen not implemented yet") }
                        // Add more destinations similarly.
                    }
                }
            }
        }
    }

    @Composable
    fun CreateSearchScreen(navController: NavController) {
        val viewModel = hiltViewModel<MainActivityViewModel>()
        val searchResultUsers = viewModel.searchResult.collectAsState()
        val query = viewModel.query.collectAsState()
        SearchUserScreen(
            query = query.value,
            searchResultUsers = searchResultUsers.value,
            placeHolder = null,
            imageLoader = imageLoader,
            onSearch = { viewModel.search(query.value) },
            onNavigate = { user ->
                navController.navigate(
                    "${NavigationC.ReposList.route}/${user.name}/${
                        user.avatar.replace(
                            "/",
                            "%2F"
                        )
                    }"
                )
            },
            onQueryChange = { viewModel.query.value = it },
            onQueryReplace = {
                viewModel.query.value = it
                viewModel.search(it)
            },
            onClearText = { viewModel.query.value = "" },
        )
    }

    @Composable
    fun CreateReposScreen(
        name: String,
        avatar: String?,
    ) {
        val viewModel = hiltViewModel<ReposViewModel>()
        viewModel.userRepos(name!!)
        val repos = viewModel.repos.collectAsState()
        ReposListScreen(
            name = name,
            avatar = avatar,
            imageLoader = imageLoader,
            repos = repos.value,
            onRepoClick = { Toast.makeText(this, "Repo $it clicked", Toast.LENGTH_SHORT).show() }
        )
    }
}