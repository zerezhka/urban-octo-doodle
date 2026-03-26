package com.example.githubexplorer.githubrepos.ui

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.BuildConfig
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.ui.userFriendlyMessage
import com.example.githubexplorer.main.usecase.ReposUseCase
import com.example.githubexplorer.network.GitHubService
import com.ketch.DownloadModel
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GithubReposViewModel @Inject constructor(
    private val reposUseCase: ReposUseCase,
    private val ketch: Ketch
) : ViewModel() {
    val repos = MutableStateFlow<List<GithubRepository>>(emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val downloadsByTag: StateFlow<Map<String, DownloadModel>> = ketch.observeDownloads()
        .map { downloads -> downloads.associateBy { it.tag } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private var request: Job? = null
    fun userRepos(userName: String) {
        request?.cancel()
        request = viewModelScope.launch(Dispatchers.IO) {
            _error.value = null
            try {
                repos.emit(reposUseCase.userRepos(userName))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Failed to load repos")
                _error.value = userFriendlyMessage(e)
            }
        }
    }

    fun downloadRepo(repo: GithubRepository) {
        ketch.download(
            url = GitHubService.zipballUrl(repo.owner, repo.name),
            headers = hashMapOf(
                "Authorization" to BuildConfig.GITHUB_TOKEN,
                "Accept" to "application/vnd.github.v3+json",
                "X-GitHub-Api-Version" to "2026-03-10"
            ),
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path,
            fileName = repo.name,
            tag = repo.url,
        )
    }
}
