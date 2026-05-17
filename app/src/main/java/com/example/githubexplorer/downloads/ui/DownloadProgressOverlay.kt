package com.example.githubexplorer.downloads.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import com.ketch.DownloadModel

@Composable
fun DownloadProgressOverlay(
    activeDownloads: List<DownloadModel>,
    onCancel: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 600.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
    ) {
        items(activeDownloads, key = { it.id }) { download ->
            DownloadProgressCard(download, onCancel, modifier = Modifier.animateItem())
        }
    }
}

@Composable
private fun DownloadProgressCard(
    download: DownloadModel,
    onCancel: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isIndeterminate = download.total <= 0L
    val animatedProgress by animateFloatAsState(
        targetValue = if (isIndeterminate) 0f else download.progress / 100f,
        animationSpec = tween(300),
        label = "downloadProgress"
    )

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    download.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                if (isIndeterminate) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Text("Downloading...", style = MaterialTheme.typography.bodySmall)
                } else {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${download.progress}%", style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = { onCancel(download.id) }) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }
        }
    }
}
