package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        setContent {
            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoading = remember { stateIsLoading }
                    if (!isLoading.value)
                        HelloScreen(clicker, innerPadding)
                    else
                        Loading(clicker)
                }
            }
        }
    }
}

@Composable
fun HelloScreen(clicker: () -> Unit = {}, innerPadding: PaddingValues) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Composabel.LoadingImageFromInternetCoil(
                model = "https://avatars.githubusercontent.com/u/10316435?v=4",
                contentDescription = "zerezhka avatar"
            )
            Greeting(
                name = "Android",
            )
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
}
@Composable
fun Loading(clicker: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().clickable(onClick = clicker), contentAlignment = Alignment.Center) {
        Text(
            text = "Loading...\n(Click again)",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Greeting(name: String) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
        )
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun GreetingPreview() {
    GithubExplorerTheme {
        HelloScreen({}, PaddingValues(0.dp))
    }
}
@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun LoadingPreview() {
    GithubExplorerTheme {
        Loading {}
    }
}