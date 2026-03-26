package com.example.githubexplorer.main.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.githubexplorer.NavRoute
import com.example.githubexplorer.downloads.ui.DownloadsScreen
import com.example.githubexplorer.githubrepos.ui.GithubReposScreen
import com.example.githubexplorer.search.ui.SearchScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.UserFinder,
        enterTransition = { slideInHorizontally(tween(300)) { it / 3 } + fadeIn(tween(300)) },
        exitTransition = { slideOutHorizontally(tween(300)) { -it / 3 } + fadeOut(tween(200)) },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it / 3 } + fadeIn(tween(300)) },
        popExitTransition = { slideOutHorizontally(tween(300)) { it / 3 } + fadeOut(tween(200)) },
        modifier = modifier
    ) {
        composable<NavRoute.UserFinder> {
            SearchScreen(navController)
        }
        composable<NavRoute.ReposList> { backStackEntry ->
            val route = backStackEntry.toRoute<NavRoute.ReposList>()
            GithubReposScreen(name = route.name, avatar = route.avatar)
        }
        composable<NavRoute.DownloadScreen> {
            DownloadsScreen()
        }
    }
}
