package com.example.githubexplorer.downloads.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.ketch.DownloadModel
import java.io.File

fun openDownloadedFile(context: Context, download: DownloadModel) {
    val file = File(download.path, download.fileName)
    if (!file.exists()) return
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/zip")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Open with"))
}
