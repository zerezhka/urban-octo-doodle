package com.example.githubexplorer.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import com.example.githubexplorer.main.data.GithubRepository


@Composable
fun ReposListScreen(
    name: String?,
    avatar: String?,
    imageLoader: ImageLoader,
    repos: List<GithubRepository>,
    onRepoClick: (repo: GithubRepository) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        LoadingImageFromInternetCoil(
            image = avatar,
            contentDescription = "$name's avatar",
            placeholder = null,
            imageLoader = imageLoader
        )
        Text(name!!)

        repos.forEach {
            RepoItem(
                repo = it,
                onRepoClick = {
                    onRepoClick.invoke(it)
                },
            )
        }
    }
}

@Composable
fun RepoItem(repo: GithubRepository, onRepoClick: (repo: GithubRepository) -> Unit) {
    Column(
        Modifier
            .clickable(onClick = { onRepoClick.invoke(repo) })
            .padding(16.dp, 8.dp)
            .fillMaxSize()
    ) {
        Text(repo.name, fontSize = 20.sp)
        Text(repo.url, fontSize = 18.sp, color = androidx.compose.ui.graphics.Color.Blue)
    }
}