package com.example.githubexplorer.main.db.dao

import androidx.room.Dao
import com.example.githubexplorer.main.db.model.GithubUserDB


/**
 * [Room] DAO for [GithubUserDB] related operations.
 */
@Dao
abstract class UsersDao: BaseDao<GithubUserDB> {
}