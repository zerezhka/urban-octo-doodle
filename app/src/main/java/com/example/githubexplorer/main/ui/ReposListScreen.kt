package com.example.githubexplorer.main.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.ui.compose.MyIcons
import com.ketch.Ketch


@Composable
fun CreateReposScreen(
    name: String,
    avatar: String?,
    imageLoader: ImageLoader,
    fileDownloader: Ketch,
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
        onDownloadClick = {
            fileDownloader.download(
                // it responds with a new url to download the file, todo move everything to the viewmodel and retrofit @Streaming api
                // todo add a progress bar
                // NOT WORKING
                url = "https://api.github.com/repos/${it.owner}/${it.name}/zipball",
                headers = hashMapOf<String, String>(
                    "Authorization" to BuildConfig.GITHUB_TOKEN,
                    "Accept" to "application/vnd.github.v3+json",
                    "X-GitHub-Api-Version" to "2022-11-28"),

                path = ".",
                fileName = it.name,
            )
            Toast.makeText(context, "Downloading ${it.name}", Toast.LENGTH_SHORT).show()
        },
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
        Text(repo.name, fontSize = 20.sp)
        Text(
            repo.url,
            fontSize = 18.sp,
            color = androidx.compose.ui.graphics.Color.Blue,
            modifier = Modifier
                .padding(0.dp, 8.dp)
                .clickable(onClick = { onRepoClick.invoke(repo) })
        )
        IconButton(onClick = {
            onDownloadClick.invoke(repo)
        }) {
            MyIcons.DownloadIcon()
        }
    }
}