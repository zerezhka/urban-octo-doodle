package com.example.githubexplorer.downloads.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DownloadOverlayViewModel @Inject constructor(
    private val ketch: Ketch
) : ViewModel() {

    private val activeStatuses = listOf(Status.QUEUED, Status.STARTED, Status.PROGRESS)

    val activeDownloads: StateFlow<List<DownloadModel>> = ketch.observeDownloads()
        .map { downloads ->
            downloads.filter { it.status in activeStatuses }.takeLast(3)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cancel(id: Int) {
        ketch.cancel(id)
    }
}
