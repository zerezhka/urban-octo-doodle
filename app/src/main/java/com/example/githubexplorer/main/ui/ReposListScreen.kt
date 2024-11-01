package com.example.githubexplorer.main.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.ui.compose.MyIcons


@Composable
fun CreateReposScreen(
    name: String,
    avatar: String?,
    imageLoader: ImageLoader,
    onDownloadClick: (GithubRepository) -> Unit
) {
    val viewModel = hiltViewModel<ReposViewModel>()
    viewModel.userRepos(name)
    val repos = viewModel.repos.collectAsState()
    val context = LocalContext.current
    ReposListScreen(
        name = name,
        avatar = avatar,
        imageLoader = imageLoader,
        repos = repos.value,
        onDownloadClick = onDownloadClick,
        onLinkClick = {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(it.url))
            val choser = Intent.createChooser(intent, "Choose browser")
            context.startActivity(choser)
        },
    )
}

@Composable
private fun ReposListScreen(
    name: String?,
    avatar: String?,
    imageLoader: ImageLoader,
    repos: List<GithubRepository>,
    onDownloadClick: (repo: GithubRepository) -> Unit,
    onLinkClick: (repo: GithubRepository) -> Unit,
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
                onRepoClick = onLinkClick,
                onDownloadClick = onDownloadClick
            )
        }
    }
}

@Composable
fun RepoItem(
    repo: GithubRepository,
    onRepoClick: (repo: GithubRepository) -> Unit,
    onDownloadClick: (GithubRepository) -> Unit
) {
    Column(
        Modifier
            .padding(16.dp, 8.dp)
            .fillMaxSize()
    ) {
        Row {
            Text(repo.name, fontSize = 20.sp)
            IconButton(onClick = {
                onDownloadClick.invoke(repo)
            }) {
                MyIcons.DownloadIcon()
            }
        }
        Text(
            repo.url,
            fontSize = 18.sp,
            color = androidx.compose.ui.graphics.Color.Blue,
            modifier = Modifier
                .padding(0.dp, 8.dp)
                .clickable(onClick = { onRepoClick.invoke(repo) })
        )
    }
}