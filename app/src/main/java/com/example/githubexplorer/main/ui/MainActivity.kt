package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.githubexplorer.main.data.GithubUser
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val stateIsLoading = viewModel.isLoading
        val clicker = {
            stateIsLoading.value = !stateIsLoading.value
//            Toast.makeText(this, "Hello! ${stateIsLoading.value}", Toast.LENGTH_SHORT).show()
        }
        // todo get from repository
        val user = GithubUser("zerezhka", "https://avatars.githubusercontent.com/u/10316435?v=4")
        setContent {
            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoading = remember { stateIsLoading }
                    if (!isLoading.value)
                        UserScreen(
                            user,
                            clicker,
                            null,
                            innerPadding,
                        )
                    else
                        Loading(clicker)
                }
            }
        }
    }
}

@Composable
fun Loading(clicker: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = clicker),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Loading...\n(Click again)",
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun GreetingPreview() {
    GithubExplorerTheme {
        UserScreen(
            GithubUser("zerezhka", "https://avatars.githubusercontent.com/u/10316435?v=4"),
            {},
            painterResource(id = com.example.githubexplorer.R.drawable.ic_launcher_background),
            PaddingValues(8.dp)
        )
    }
}
/*
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun LoadingPreview() {
    GithubExplorerTheme {
        Loading {}
    }
}*/
