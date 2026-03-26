package com.example.githubexplorer.main.ui

import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun userFriendlyMessage(e: Exception): String = when (e) {
    is SocketTimeoutException -> "Connection timed out. Check your internet."
    is UnknownHostException -> "No internet connection."
    is retrofit2.HttpException -> when (e.code()) {
        401, 403 -> "Authentication failed. Check your GitHub token."
        404 -> "Not found."
        429 -> "Too many requests. Try again later."
        in 500..599 -> "GitHub is having issues. Try again later."
        else -> "Request failed (${e.code()})."
    }
    else -> "Something went wrong. Try again."
}
