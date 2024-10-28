package com.example.githubexplorer.main.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubexplorer.main.db.dao.ReposDao
import com.example.githubexplorer.main.db.dao.UsersDao
import com.example.githubexplorer.main.db.model.GithubRepoDB
import com.example.githubexplorer.main.db.model.GithubUserDB

/**
 * The [RoomDatabase] we use in this app.
 */
@Database(
    entities = [
        GithubUserDB::class,
        GithubRepoDB::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GithubExploraDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun reposDao(): ReposDao
}