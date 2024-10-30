package com.example.githubexplorer.main.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val useCase: SearchUseCase) : ViewModel() {
    var isLoading: MutableState<Boolean> = mutableStateOf(false)
    var searchResult: MutableStateFlow<List<GithubUser>> = MutableStateFlow(emptyList())

    //first state whether the search is happening or not
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    var query: MutableStateFlow<String> = MutableStateFlow("")

    private var request: Job? = null

    fun search(query: String) {
        request?.cancel()
        request = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                isLoading.value = true
                val result = async { useCase.search(query) }
                searchResult.emit(result.await())
                isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        request?.cancel()
        request = null
    }
}