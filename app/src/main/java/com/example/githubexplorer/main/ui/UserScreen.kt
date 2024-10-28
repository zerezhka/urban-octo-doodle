package com.example.githubexplorer.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.example.githubexplorer.main.data.GithubUser
import timber.log.Timber

@Composable
fun UserScreen(
    user: GithubUser,
    clicker: () -> Unit,
    placeHolder: Painter?,
    innerPadding: PaddingValues,
    imageLoader: ImageLoader?,
) {
    Column(
//        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LoadingImageFromInternetCoil(
                model = user.avatar,
                contentDescription = "${user.name} avatar",
                placeholder = placeHolder,
                imageLoader = imageLoader,
            )
        }
        Text(user.name)
        Button(
            onClick = clicker,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            enabled = true,
            contentPadding = PaddingValues(12.dp)
        ) {
            Text("Click me!")
        }
    }
}

@Composable
fun LoadingImageFromInternetCoil(
    model: Any?,
    contentDescription: String?,
    placeholder: Painter?,
    imageLoader: ImageLoader?,
) {
    if (imageLoader != null) {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = remember {
                Timber.d("Loading: $model")
                model
            },
            contentDescription = contentDescription,
            placeholder = remember { placeholder },
            imageLoader = imageLoader,
        )
    } else {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = remember {
                model
            },
            contentDescription = contentDescription,
            placeholder = remember { placeholder },
        )
    }
}

@Composable
fun LoadingScreen(onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick)
    ) {
        Text("Loading...")
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_6A)
@Composable
fun PreviewUserScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPaddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            UserScreen(
                GithubUser("username", "avatar_url"),
                { },
                painterResource(id = com.example.githubexplorer.R.drawable.ic_launcher_background),
                innerPaddings,
                null
            )
        }
    }
}