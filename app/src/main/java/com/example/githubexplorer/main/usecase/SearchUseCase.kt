package com.example.githubexplorer.main.usecase

import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.repository.SearchRepository
import javax.inject.Inject


open class SearchUseCase @Inject constructor(val searchRepository: SearchRepository) {
    suspend fun search(text: String): List<GithubUser> {
        return searchRepository.search(text)
    }
}