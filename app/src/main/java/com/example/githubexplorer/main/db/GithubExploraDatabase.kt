package com.example.githubexplorer.main.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubexplorer.main.db.dao.ReposDao
import com.example.githubexplorer.main.db.dao.UsersDao
import com.example.githubexplorer.main.db.model.GithubRepoDB
import com.example.githubexplorer.main.db.model.GithubUserDB

private const val DATABASE_VERSION = 2

/**
 * The [RoomDatabase] we use in this app.
 */
@Database(
    entities = [
        GithubUserDB::class,
        GithubRepoDB::class
    ],
    version = DATABASE_VERSION,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class GithubExploraDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun reposDao(): ReposDao
}