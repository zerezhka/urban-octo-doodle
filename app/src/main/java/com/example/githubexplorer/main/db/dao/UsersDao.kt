package com.example.githubexplorer.main.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.githubexplorer.main.db.model.GithubUserDB


/**
 * [Room] DAO for [GithubUserDB] related operations.
 */
@Dao
abstract class UsersDao: BaseDao<GithubUserDB> {
    @Query("SELECT * FROM users WHERE username LIKE  '%'||:name||'%'")
    abstract fun getAllContaining(name: String): List<GithubUserDB>
}