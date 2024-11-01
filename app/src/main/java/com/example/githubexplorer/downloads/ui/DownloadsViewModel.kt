package com.example.githubexplorer.downloads.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketch.DownloadModel
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(val ketch: Ketch): ViewModel() {
    val downloads: MutableStateFlow<List<DownloadModel>> = MutableStateFlow(emptyList())
    fun getKetchDownloads() {
        viewModelScope.launch(Dispatchers.IO) {
            downloads.emit( ketch.getAllDownloads())
        }
    }
}