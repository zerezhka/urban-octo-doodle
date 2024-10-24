package com.example.githubexplorer.main.ui

import android.os.Build.VERSION
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.githubexplorer.base.mvp.BaseMvpActivity
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseMvpActivity<MainScreenContract.View, MainScreenContract.Presenter>(),MainScreenContract.View {

    override val presenter: MainActivityPresenter by lazy {
        ViewModelProvider(this as ViewModelStoreOwner)[MainActivityPresenter::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (VERSION.SDK_INT >= 31) {
            installSplashScreen()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    override fun showLoading() {
        // представим реализовал
    }

    override fun showError() {
        TODO("Not yet implemented")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            textAlign = TextAlign.Center,
            modifier = modifier
        )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GithubExplorerTheme {
        Greeting("Android")
    }
}