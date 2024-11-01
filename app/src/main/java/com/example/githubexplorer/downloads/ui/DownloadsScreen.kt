package com.example.githubexplorer.downloads.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DownloadsScreen() {
    var hasPermission = remember { mutableStateOf(false) }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        requestPostNotificationPermission(hasPermission)
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
    if (!hasPermission.value) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            Text("Please grant the permission to continue")
            Button(onClick = { requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS) }) {
                Text("Got it!")
            }
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
            Text("TODO:Get viewModel, and draw the list of downloads")
        }
    }
}
