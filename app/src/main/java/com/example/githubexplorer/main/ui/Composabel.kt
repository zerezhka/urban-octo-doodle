package com.example.githubexplorer.main.ui

import androidx.compose.runtime.Composable
import coil3.compose.AsyncImage

object Composabel {

    @Composable
    fun LoadingImageFromInternetCoil(model: String, contentDescription:String?) {
        // [START android_compose_images_load_internet_coil]
        AsyncImage(
            model = model,
            contentDescription = contentDescription
        )
        // [END android_compose_images_load_internet_coil]
    }
}