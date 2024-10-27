package com.example.githubexplorer.main.repository

import com.example.githubexplorer.main.data.GithubRepository
import com.example.githubexplorer.main.data.GithubUser

interface Gateway {
    fun search(query: String): List<GithubUser>
    fun getRepositories(user: String): List<GithubRepository>
}