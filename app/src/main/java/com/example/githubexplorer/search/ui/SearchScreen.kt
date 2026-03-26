package com.example.githubexplorer.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.githubexplorer.NavigationC
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.ui.compose.MyIcons
import timber.log.Timber

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val searchResultUsers = viewModel.searchResult.collectAsState()
    val query = viewModel.query.collectAsState()
    val isLoading = viewModel.isLoading.value
    SearchScreenContent(
        query = query.value,
        searchResultUsers = searchResultUsers.value,
        isLoading = isLoading,
        onSearch = { viewModel.search(query.value) },
        onNavigateToUser = { user ->
            navController.navigate(NavigationC.ReposList(name = user.name, avatar = user.avatar))
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
private fun SearchScreenContent(
    query: String,
    searchResultUsers: List<GithubUser>,
    isLoading: Boolean,
    onSearch: () -> Unit,
    onQueryChange: (String) -> Unit,
    onQueryReplace: (String) -> Unit,
    onClearText: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToUser: ((GithubUser) -> Unit) = {},
    onNavigateToDownloads: (() -> Unit) = {},
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
            Text("type username, e.g. ")
            Text(
                "zerezhka", color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = {
                        expanded = false
                        onQueryReplace.invoke("zerezhka")
                    }
                )
            )
        }

        AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut()) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        }

        AnimatedVisibility(visible = searchResultUsers.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
            Column {
                searchResultUsers.forEach { user ->
                    UserRow(user, onNavigateToUser)
                }
            }
        }
    }
}

@Composable
private fun UserRow(
    user: GithubUser,
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
        AsyncImage(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp)),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(user.avatar)
                .build(),
            contentDescription = "${user.name} avatar",
        )
        Text(user.name, modifier = Modifier.padding(16.dp))
    }
}
