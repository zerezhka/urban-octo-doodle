package com.example.githubexplorer.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.githubexplorer.main.db.model.GithubRepoDB


/**
 * [Room] DAO for [GithubRepoDB] related operations.
 */
@Dao
abstract class ReposDao: BaseDao<GithubRepoDB> {
    @Query("SELECT * FROM repos WHERE name = :user")
    abstract fun getReposByUser(user: String): List<GithubRepoDB>
}