package com.example.githubexplorer.main.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.githubexplorer.base.mvp.BaseMvpActivity
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseMvpActivity<MainScreenContract.View, MainScreenContract.Presenter>(),
    MainScreenContract.View {

    override val presenter: MainActivityPresenter by lazy {
        ViewModelProvider(this as ViewModelStoreOwner)[MainActivityPresenter::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val clicker = { Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT).show() }
        setContent {
            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isLoading = remember { mutableStateOf(true) }
                    if (!isLoading.value)
                        Greeting(
                            clicker,
                            name = "Android",
                            modifier = Modifier
                                .padding(innerPadding)
                                .safeContentPadding()
                                .safeDrawingPadding(),
                        ) else Loading()
                }
            }
        }
    }

    override fun showLoading() {
        // представим реализовал
    }

    @Composable
    fun Loading() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Loading...",
                textAlign = TextAlign.Center
            )
        }
    }

    override fun showError() {
        TODO("Not yet implemented")
    }
}

@Composable
fun Greeting(onClick: () -> Unit, name: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun GreetingPreview() {
    GithubExplorerTheme {
        Greeting({}, "Android")
    }
}