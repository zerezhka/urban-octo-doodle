package com.example.githubexplorer.main.db.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "repos",
    foreignKeys = [
        ForeignKey(
            entity = GithubUserDB::class,
            parentColumns = ["username"],
            childColumns = ["owner"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@Immutable
data class GithubRepoDB(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "owner")
    val owner: String,
    @ColumnInfo(name = "isDownloaded", defaultValue = "false")
    val isDownload: Boolean,
    // for open file in filesystem
    @ColumnInfo(name = "downloadedUri", defaultValue = "")
    val downloadedUri: String,
    @ColumnInfo(name = "downloadUrl", defaultValue = "")
    val downloadUrl: String,

    @PrimaryKey val id: String = "$owner/$name"
)
