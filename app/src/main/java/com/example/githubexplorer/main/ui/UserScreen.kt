package com.example.githubexplorer.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.githubexplorer.main.data.GithubUser
import timber.log.Timber

@Composable
fun UserScreen(
    user: GithubUser,
    clicker: () -> Unit,
    placeHolder: Painter?,
    innerPadding: PaddingValues
) {

    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            LoadingImageFromInternetCoil(
                model = user.avatar,
                contentDescription = "${user.name} avatar",
                placeholder = placeHolder
            )
            UserScreen(user, clicker, placeHolder, innerPadding)
        }
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
    Text(user.name)
}

@Composable
fun LoadingImageFromInternetCoil(
    model: Any?, contentDescription: String?, placeholder: Painter?
) {
    // [START android_compose_images_load_internet_coil]
    AsyncImage(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(24.dp)),
        model = remember {
            Timber.d("Loading: $model")
            model
        },
        contentDescription = contentDescription,
        placeholder = remember { placeholder }
    )
    // [END android_compose_images_load_internet_coil]
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
                innerPaddings
            )
        }
    }
}