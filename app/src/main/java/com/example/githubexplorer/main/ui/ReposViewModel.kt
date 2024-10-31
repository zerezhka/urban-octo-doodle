package com.example.githubexplorer.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.usecase.ReposUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(private val reposUseCase: ReposUseCase): ViewModel() {
    val repos : MutableStateFlow<List<GithubRepository>> = MutableStateFlow(emptyList())

    private var request: Job? = null
    fun userRepos(userName: String) {
        request?.cancel()
        request = viewModelScope.launch(Dispatchers.IO) {
            val req = async{
               reposUseCase.userRepos(userName)
            }
            repos.emit(req.await())
        }
    }
    fun downloadRepo(repo: GithubRepository) = reposUseCase.downloadRepos(repo)
}