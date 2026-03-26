package com.example.githubexplorer.downloads.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ketch.DownloadModel
import com.ketch.Status

@Composable
fun DownloadsScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val hasPermission = remember { mutableStateOf(false) }
        RequestPostNotificationPermission(hasPermission)
    } else {
        ShowDownloadsContent()
    }
}

@Composable
private fun ShowDownloadsContent() {
    val viewModel = hiltViewModel<DownloadsViewModel>()
    val downloads by viewModel.downloads.collectAsState()
    val context = LocalContext.current

    val onOpenFile: (DownloadModel) -> Unit = { openDownloadedFile(context, it) }

    if (downloads.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No downloads yet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(downloads, key = { it.id }) { download ->
                DownloadItem(
                    download = download,
                    onRetry = { viewModel.retry(it) },
                    onCancel = { viewModel.cancel(it) },
                    onPause = { viewModel.pause(it) },
                    onResume = { viewModel.resume(it) },
                    onDelete = { viewModel.delete(it) },
                    onOpenFile = onOpenFile,
                )
            }
        }
    }
}

@Composable
private fun DownloadItem(
    download: DownloadModel,
    onRetry: (Int) -> Unit,
    onCancel: (Int) -> Unit,
    onPause: (Int) -> Unit,
    onResume: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onOpenFile: (DownloadModel) -> Unit,
) {
    val isCompleted = download.status == Status.SUCCESS
    val isActive = download.status in listOf(Status.QUEUED, Status.STARTED, Status.PROGRESS)
    val isIndeterminate = download.total <= 0L
    val animatedProgress by animateFloatAsState(
        targetValue = if (isIndeterminate || !isActive) 0f else download.progress / 100f,
        animationSpec = tween(300),
        label = "downloadItemProgress"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (isCompleted) Modifier.clickable { onOpenFile(download) } else Modifier)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                download.fileName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val owner = download.tag
                .removePrefix("https://github.com/")
                .substringBefore("/")
                .ifEmpty { null }
            if (owner != null) {
                Text(
                    owner,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                statusText(download),
                style = MaterialTheme.typography.bodySmall,
                color = statusColor(download.status)
            )

            if (isActive) {
                Spacer(Modifier.height(8.dp))
                if (isIndeterminate) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${download.progress}%", style = MaterialTheme.typography.bodySmall)
                }
            }

            if (download.status == Status.FAILED && download.failureReason.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    download.failureReason,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (download.status) {
                    Status.FAILED -> {
                        FilledTonalButton(onClick = { onRetry(download.id) }) {
                            Text("Retry")
                        }
                        TextButton(onClick = { onDelete(download.id) }) {
                            Text("Delete")
                        }
                    }
                    Status.PAUSED -> {
                        FilledTonalButton(onClick = { onResume(download.id) }) {
                            Text("Resume")
                        }
                        TextButton(onClick = { onCancel(download.id) }) {
                            Text("Cancel")
                        }
                    }
                    Status.QUEUED, Status.STARTED, Status.PROGRESS -> {
                        TextButton(onClick = { onPause(download.id) }) {
                            Text("Pause")
                        }
                        TextButton(onClick = { onCancel(download.id) }) {
                            Text("Cancel")
                        }
                    }
                    Status.SUCCESS, Status.CANCELLED -> {
                        TextButton(onClick = { onDelete(download.id) }) {
                            Text("Delete")
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun statusColor(status: Status) = when (status) {
    Status.SUCCESS -> MaterialTheme.colorScheme.primary
    Status.FAILED -> MaterialTheme.colorScheme.error
    Status.PAUSED -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}

private fun statusText(download: DownloadModel) = when (download.status) {
    Status.QUEUED -> "Queued"
    Status.STARTED -> "Starting..."
    Status.PROGRESS -> if (download.total > 0) "${download.progress}%" else "Downloading..."
    Status.SUCCESS -> "Completed"
    Status.FAILED -> "Failed"
    Status.PAUSED -> "Paused"
    Status.CANCELLED -> "Cancelled"
    else -> ""
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("ComposableNaming")
@Composable
private fun RequestPostNotificationPermission(hasPermission: MutableState<Boolean>) {
    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasPermission.value = it }
    )
    LaunchedEffect("requestPermission") {
        requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
    if (!hasPermission.value) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Please grant the permission to continue")
            Button(onClick = { requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS) }) {
                Text("Got it!")
            }
        }
    } else {
        ShowDownloadsContent()
    }
}
