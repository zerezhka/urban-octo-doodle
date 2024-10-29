package com.example.githubexplorer.main.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.example.githubexplorer.R
import com.example.githubexplorer.main.data.GithubUser
import timber.log.Timber

@SuppressLint("StateFlowValueCalledInComposition")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    users: List<GithubUser>,
    queryText: String,
    search: () -> Unit,
    onQueryChange: (query:String) -> Unit,
    placeHolder: Painter?,
    innerPadding: PaddingValues,
    imageLoader: ImageLoader?,
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var expanded = remember { false }
        SearchBar(
            inputField = {
                InputField(
                    onSearch = {
                        expanded = false
//                        onQueryChange("")//?
                        search.invoke()
                    },
                    expanded = expanded,
                    query = queryText,
                    onQueryChange = onQueryChange,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Default.Clear, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {}

        Row {
            Text("type username, e.g.")
            Text(
                "zerezhka", color = Color(20, 20, 255, 255),
                modifier = Modifier.clickable(
                    onClick = {
//                        expanded = false
                        onQueryChange("zerezhka")
//                        search.invoke()
                    }
                )
            )
        }
        users.forEach { user ->
            Row(modifier = Modifier.fillMaxWidth()) {
                LoadingImageFromInternetCoil(
                    model = user.avatar,
                    contentDescription = "${user.name} avatar",
                    placeholder = placeHolder,
                    imageLoader = imageLoader,
                )
                Text(user.name, modifier = Modifier.padding(16.dp))
            }
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

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_6A)
@Composable
fun PreviewUserScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPaddings ->
        UserScreen(
            listOf(
                GithubUser("username", "avatar_url"),
                GithubUser("username", "avatar_url"),
                GithubUser("username", "avatar_url"),
                GithubUser("username", "avatar_url"),
                GithubUser("username", "avatar_url"),
                GithubUser("username", "avatar_url"),
            ),
//            { },
            queryText = "query",
            search = {
                //presenter.search(it)
            },
            onQueryChange = { },
            painterResource(id = R.drawable.ic_launcher_background),
            innerPaddings,
            null
        )
    }
}