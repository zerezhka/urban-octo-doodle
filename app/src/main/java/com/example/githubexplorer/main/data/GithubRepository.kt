package com.example.githubexplorer.main.data

import com.example.githubexplorer.downloads.data.Download
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepository(val name: String, val url: String, val isDownloaded: Download.Status)