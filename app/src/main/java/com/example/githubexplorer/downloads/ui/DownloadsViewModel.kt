package com.example.githubexplorer.downloads.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketch.DownloadModel
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(private val ketch: Ketch) : ViewModel() {
    val downloads: StateFlow<List<DownloadModel>> = ketch.observeDownloads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun retry(id: Int) = ketch.retry(id)
    fun cancel(id: Int) = ketch.cancel(id)
    fun pause(id: Int) = ketch.pause(id)
    fun resume(id: Int) = ketch.resume(id)
}
