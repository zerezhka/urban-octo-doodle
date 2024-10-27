package com.example.githubexplorer.main.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.githubexplorer.main.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(val useCase: SearchUseCase) : ViewModel() {
    var isLoading: MutableState<Boolean> = mutableStateOf(false)
}