package com.example.githubexplorer.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.ui.userFriendlyMessage
import com.example.githubexplorer.main.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val useCase: SearchUseCase) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _searchResult = MutableStateFlow<List<GithubUser>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private var request: Job? = null

    fun updateQuery(value: String) { _query.value = value }
    fun clearQuery() { _query.value = "" }

    fun search(query: String) {
        request?.cancel()
        request = viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            try {
                _searchResult.emit(useCase.search(query))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Timber.e(e, "Search failed")
                _error.value = userFriendlyMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
