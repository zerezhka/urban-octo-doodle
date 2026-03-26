package com.example.githubexplorer.repos.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.ui.compose.MyIcons
import com.ketch.DownloadModel
import com.ketch.Status
import java.io.File

@Composable
fun ReposScreen(name: String, avatar: String?) {
    val viewModel = hiltViewModel<ReposViewModel>()
    viewModel.userRepos(name)
    val repos by viewModel.repos.collectAsState()
    val downloadsByTag by viewModel.downloadsByTag.collectAsState()
    val isLoading = repos.isEmpty()
    val context = LocalContext.current
    ReposScreenContent(
        name = name,
        avatar = avatar,
        repos = repos,
        downloadsByTag = downloadsByTag,
        isLoading = isLoading,
        onDownloadClick = { viewModel.downloadRepo(it) },
        onLinkClick = {
            val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(it.url))
            context.startActivity(Intent.createChooser(intent, "Choose browser"))
        },
        onOpenFile = { download ->
            val file = File(download.path, download.fileName)
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/zip")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Open with"))
            }
        },
    )
}

@Composable
private fun ReposScreenContent(
    name: String?,
    avatar: String?,
    repos: List<GithubRepository>,
    downloadsByTag: Map<String, DownloadModel>,
    isLoading: Boolean,
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

        AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                LinearProgressIndicator()
            }
        }

        AnimatedVisibility(visible = repos.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repos.forEach { repo ->
                    val download = downloadsByTag[repo.url]
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

@Composable
private fun RepoItem(
    repo: GithubRepository,
    download: DownloadModel?,
    onRepoClick: (repo: GithubRepository) -> Unit,
    onDownloadClick: (GithubRepository) -> Unit,
    onOpenFile: (DownloadModel) -> Unit,
) {
    val isDownloaded = download?.status == Status.SUCCESS

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
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

                if (isDownloaded) {
                    IconButton(onClick = { onOpenFile(download!!) }) {
                        Icon(
                            imageVector = FolderOpenIcon,
                            contentDescription = "Open file",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    IconButton(onClick = { onDownloadClick(repo) }) {
                        MyIcons.DownloadIcon()
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

            if (isDownloaded) {
                Text("Downloaded", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 2.dp))
            }
        }
    }
}

private val FolderOpenIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "FolderOpen", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 960f, viewportHeight = 960f
    ).apply {
        path(fill = SolidColor(Color.Black), pathFillType = PathFillType.NonZero) {
            moveTo(160f, 800f); quadToRelative(-33f, 0f, -56.5f, -23.5f); reflectiveQuadTo(80f, 720f)
            verticalLineToRelative(-480f); quadToRelative(0f, -33f, 23.5f, -56.5f); reflectiveQuadTo(160f, 160f)
            horizontalLineToRelative(240f); lineToRelative(80f, 80f); horizontalLineToRelative(320f)
            quadToRelative(33f, 0f, 56.5f, 23.5f); reflectiveQuadTo(880f, 320f)
            horizontalLineTo(447f); lineToRelative(-80f, -80f); horizontalLineTo(160f)
            verticalLineToRelative(480f); lineToRelative(96f, -320f); horizontalLineToRelative(684f)
            lineToRelative(-108f, 360f); close()
        }
    }.build()
}
