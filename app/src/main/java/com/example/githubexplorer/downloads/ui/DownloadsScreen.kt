package com.example.githubexplorer.downloads.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ketch.Status

@Composable
fun DownloadsScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        var hasPermission = remember { mutableStateOf(false) }
        requestPostNotificationPermission(hasPermission)
    } else {
        ShowDownloadsScreen()
    }
}

@Composable
fun ShowDownloadsScreen() {
    val viewModel = hiltViewModel<DownloadsViewModel>()
    viewModel.getKetchDownloads()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val downloads = viewModel.downloads.collectAsState()
        downloads.value.forEach {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Download: ${it.url}")
                Text("status: ${it.status}")
                if (it.status == Status.FAILED) {
                    Text(it.failureReason)
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("ComposableNaming")
@Composable
fun requestPostNotificationPermission(hasPermission: MutableState<Boolean>) {
    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { hasPermission.value = it }
    )
    LaunchedEffect("requestPermission") {
        // throws launcher has not been initialized, if not wrapped in LaunchedEffect
        requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }
    if (!hasPermission.value) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Please grant the permission to continue")
            Button(onClick = { requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS) }) {
                Text("Got it!")
            }
        }
    } else {
        ShowDownloadsScreen()
    }
}
