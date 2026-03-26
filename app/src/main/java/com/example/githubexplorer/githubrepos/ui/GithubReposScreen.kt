package com.example.githubexplorer.githubrepos.ui

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.githubexplorer.downloads.ui.openDownloadedFile
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.ui.compose.AppIcons
import com.ketch.DownloadModel
import com.ketch.Status

@Composable
fun GithubReposScreen(name: String, avatar: String?) {
    val viewModel = hiltViewModel<GithubReposViewModel>()
    viewModel.userRepos(name)
    val repos by viewModel.repos.collectAsState()
    val downloadsByTag by viewModel.downloadsByTag.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading = repos.isEmpty() && error == null
    val context = LocalContext.current
    ReposScreenContent(
        name = name,
        avatar = avatar,
        repos = repos,
        downloadsByTag = downloadsByTag,
        isLoading = isLoading,
        error = error,
        onDownloadClick = { viewModel.downloadRepo(it) },
        onLinkClick = {
            val intent = Intent(Intent.ACTION_VIEW, it.url.toUri())
            context.startActivity(Intent.createChooser(intent, "Choose browser"))
        },
        onOpenFile = { openDownloadedFile(context, it) },
    )
}

@Composable
private fun ReposScreenContent(
    name: String?,
    avatar: String?,
    repos: List<GithubRepository>,
    downloadsByTag: Map<String, DownloadModel>,
    isLoading: Boolean,
    error: String?,
    onDownloadClick: (repo: GithubRepository) -> Unit,
    onLinkClick: (repo: GithubRepository) -> Unit,
    onOpenFile: (download: DownloadModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(avatar)
                .build(),
            contentDescription = "$name's avatar",
        )
        Text(name!!, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp, 4.dp))

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                LinearProgressIndicator()
            }
        }

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Text(
                error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        AnimatedVisibility(
            visible = repos.isNotEmpty(),
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(200))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repos.forEachIndexed { index, repo ->
                    val download = downloadsByTag[repo.url]
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            initialOffsetY = { it }
                        ) + fadeIn(tween(durationMillis = 300, delayMillis = index * 40)),
                    ) {
                        RepoItem(
                            repo = repo,
                            download = download,
                            onRepoClick = onLinkClick,
                            onDownloadClick = onDownloadClick,
                            onOpenFile = onOpenFile,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepoItem(
    repo: GithubRepository,
    download: DownloadModel?,
    onRepoClick: (repo: GithubRepository) -> Unit,
    onDownloadClick: (GithubRepository) -> Unit,
    onOpenFile: (DownloadModel) -> Unit,
) {
    val isDownloaded = download?.status == Status.SUCCESS

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(spring(stiffness = Spring.StiffnessLow))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        repo.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!repo.description.isNullOrBlank()) {
                        Text(
                            repo.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                AnimatedContent(
                    targetState = isDownloaded,
                    transitionSpec = {
                        (scaleIn(tween(300)) + fadeIn(tween(300)))
                            .togetherWith(scaleOut(tween(200)) + fadeOut(tween(200)))
                    },
                    label = "downloadIconTransition"
                ) { downloaded ->
                    if (downloaded) {
                        IconButton(onClick = { onOpenFile(download!!) }) {
                            Icon(
                                imageVector = AppIcons.FolderOpen,
                                contentDescription = "Open file",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        IconButton(onClick = { onDownloadClick(repo) }) {
                            AppIcons.DownloadIcon()
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!repo.language.isNullOrBlank()) {
                    Text(repo.language, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                }
                if (repo.stars > 0) {
                    Text("\u2605 ${repo.stars}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Text(
                repo.url,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp).clickable(onClick = { onRepoClick(repo) })
            )

            AnimatedVisibility(
                visible = isDownloaded,
                enter = fadeIn(tween(300)) + slideInVertically(tween(300)),
                exit = fadeOut(tween(200))
            ) {
                Text(
                    "Downloaded",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
