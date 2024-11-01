package com.example.githubexplorer.main.ui

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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.ui.compose.MyIcons
import timber.log.Timber


@Composable
fun CreateSearchScreen(navController: NavController, imageLoader: ImageLoader) {
    val viewModel = hiltViewModel<MainActivityViewModel>()
    val searchResultUsers = viewModel.searchResult.collectAsState()
    val query = viewModel.query.collectAsState()
    SearchUserScreen(
        query = query.value,
        searchResultUsers = searchResultUsers.value,
        placeHolder = null,
        imageLoader = imageLoader,
        onSearch = { viewModel.search(query.value) },
        onNavigateToUser = { user ->
            navController.navigate(
                "${NavigationC.ReposList.ROUTE}/${user.name}/${
                    //todo don't use avatar in navigation, just take it from db
                    user.avatar.replace(
                        "/",
                        "%2F"
                    )
                }"
            )
        },
        onNavigateToDownloads = {
            navController.navigate(NavigationC.DownloadScreen)
        },
        onQueryChange = { viewModel.query.value = it },
        onQueryReplace = {
            viewModel.query.value = it
            viewModel.search(it)
        },
        onClearText = { viewModel.query.value = "" },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchUserScreen(
    query: String,
    searchResultUsers: List<GithubUser>,
    placeHolder: Painter?,
    imageLoader: ImageLoader?,
    onNavigateToUser: ((GithubUser) -> Unit) = {},
    onNavigateToDownloads: (() -> Unit) = {},
    onSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    onQueryReplace: (String) -> Unit,
    onClearText: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var expanded = remember { false }
        IconButton(
            onClick = onNavigateToDownloads, modifier = Modifier.align(Alignment.End),
        ) {
            MyIcons.DownloadIcon()
        }
        SearchBar(
            inputField = {
                InputField(
                    onSearch = {
                        expanded = false
                        onSearch.invoke()
                    },
                    expanded = expanded,
                    query = query,
                    onQueryChange = onQueryChange,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.clickable { onClearText.invoke() })
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
                        onQueryReplace.invoke("zerezhka")

                    }
                )
            )
        }
        searchResultUsers.forEach { user ->
            UserRow(user, placeHolder, imageLoader, onNavigateToUser)
        }
    }
}

@Composable
private fun UserRow(
    user: GithubUser,
    placeholder: Painter?,
    imageLoader: ImageLoader?,
    onUserClick: (GithubUser) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                Timber.d("UserRow: $user")
                onUserClick(user)
            })
    ) {
        LoadingImageFromInternetCoil(
            image = user.avatar,
            contentDescription = "${user.name} avatar",
            placeholder = placeholder,
            imageLoader = imageLoader,
        )
        Text(user.name, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun LoadingImageFromInternetCoil(
    image: String?,
    contentDescription: String?,
    placeholder: Painter?,
    imageLoader: ImageLoader?,
) {
    if (imageLoader != null) {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(image)
                .build(),
            contentDescription = contentDescription,
            placeholder = placeholder,
            imageLoader = imageLoader,
        )
    } else {
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(image)
                .build(),
            contentDescription = contentDescription,
            placeholder = placeholder,
        )
    }
}