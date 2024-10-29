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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.example.githubexplorer.main.data.GithubUser
import timber.log.Timber

@SuppressLint("StateFlowValueCalledInComposition")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: MainActivityViewModel = hiltViewModel(),
    placeHolder: Painter?,
    imageLoader: ImageLoader?,
    onNavigate: ((GithubUser) -> Unit) = {},
    modifier: Modifier = Modifier
) {

    val searchResultUsers = viewModel.searchResult.collectAsState()
    val query = viewModel.query.collectAsState()
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var expanded = remember { false }
        SearchBar(
            inputField = {
                InputField(
                    onSearch = {
                        expanded = false
                        viewModel.search(it)
                    },
                    expanded = expanded,
                    query = query.value,
                    onQueryChange = { viewModel.query.value = it },
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable { viewModel.query.value = "" })
                    },
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
                        expanded = false
                        viewModel.query.value = "zerezhka"
                    }
                )
            )
        }
        searchResultUsers.value.forEach { user ->
            UserRow(user, placeHolder, imageLoader, onNavigate)
        }
    }
}

@Composable
fun UserRow(
    user: GithubUser,
    placeholder: Painter?,
    imageLoader: ImageLoader?,
    onUserClick: (GithubUser) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {
            Timber.d("UserRow: $user")
            onUserClick(user)
        })
    ) {
        Timber.d("UserRow: $user")
        LoadingImageFromInternetCoil(
            model = user.avatar,
            contentDescription = "${user.name} avatar",
            placeholder = placeholder,
            imageLoader = imageLoader,
        )
        Text(user.name, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun LoadingImageFromInternetCoil(
    model: Any?,
    contentDescription: String?,
    placeholder: Painter?,
    imageLoader: ImageLoader?,
) {
    val image = remember { model }
    if (imageLoader != null) {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = image,
            contentDescription = contentDescription,
            placeholder = remember { placeholder },
            imageLoader = imageLoader,
        )
    } else {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = image,
            contentDescription = contentDescription,
            placeholder = remember { placeholder },
        )
    }
}
/*

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_6A)
@Composable
fun PreviewUserScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPaddings ->
        UserScreen(
            viewModel = hiltViewModel(),
            onNavigate = { },
            imageLoader = null,
            modifier = Modifier.padding(innerPaddings),
            placeHolder = painterResource(id = R.drawable.ic_launcher_background)
        )
    }
}*/
