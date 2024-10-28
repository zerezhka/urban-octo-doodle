package com.example.githubexplorer.main.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val useCase: SearchUseCase) : ViewModel() {
    var isLoading: MutableState<Boolean> = mutableStateOf(false)
    var searchResult: MutableStateFlow<List<GithubUser>> = MutableStateFlow(emptyList())


    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
                    isLoading.value = true
                    val result = async { useCase.search(query) }
                    searchResult.value = result.await()
                    isLoading.value = false

        }
    }
}