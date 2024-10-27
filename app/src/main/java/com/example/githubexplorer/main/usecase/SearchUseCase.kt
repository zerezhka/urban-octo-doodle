package com.example.githubexplorer.main.usecase

import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.repository.SearchRepository


class SearchUseCase(val searchRepository: SearchRepository) {
    fun search(text: String): List<GithubUser> {
        return searchRepository.search(text)
    }
}