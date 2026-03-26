package com.example.githubexplorer.search.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.getValue
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
import com.example.githubexplorer.NavRoute
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.ui.compose.AppIcons
import timber.log.Timber

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val searchResultUsers by viewModel.searchResult.collectAsState()
    val query by viewModel.query.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    SearchScreenContent(
        query = query,
        searchResultUsers = searchResultUsers,
        isLoading = isLoading,
        error = error,
        onSearch = { viewModel.search(query) },
        onNavigateToUser = { user ->
            navController.navigate(NavRoute.ReposList(name = user.name, avatar = user.avatar))
        },
        onNavigateToDownloads = {
            navController.navigate(NavRoute.DownloadScreen)
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
    error: String?,
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
            AppIcons.DownloadIcon()
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

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(tween(200)) + slideInVertically(tween(300)),
            exit = fadeOut(tween(200))
        ) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        }

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Text(
                error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        AnimatedVisibility(
            visible = searchResultUsers.isNotEmpty(),
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(200))
        ) {
            Column {
                searchResultUsers.forEachIndexed { index, user ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            ),
                            initialOffsetY = { it * (index + 1) }
                        ) + fadeIn(tween(durationMillis = 300, delayMillis = index * 60)),
                    ) {
                        UserRow(user, onNavigateToUser)
                    }
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
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
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
        Text(
            user.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
