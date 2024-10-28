package com.example.githubexplorer.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil3.compose.AsyncImage
import com.example.githubexplorer.main.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
                        HelloScreen(
                            clicker,
                            "https://avatars.githubusercontent.com/u/10316435?v=4",
                            null,
                            innerPadding
                        )
                    else
                        Loading(clicker)
                }
            }
        }
    }
}

@Composable
fun HelloScreen(
    clicker: () -> Unit = {},
    model: Any?,
    placeHolder: Painter?,
    innerPadding: PaddingValues
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
    ) {
        LoadingImageFromInternetCoil(
            model = model,
            contentDescription = "zerezhka avatar",
            placeholder = placeHolder
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

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        textAlign = TextAlign.Center,
    )
}


@Composable
fun LoadingImageFromInternetCoil(
    model: Any?, contentDescription: String?, placeholder: Painter?
) {
    Timber.d("Loading: $model")
    // [START android_compose_images_load_internet_coil]
    AsyncImage(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(24.dp)),
        model = remember { model },
        contentDescription = contentDescription,
        placeholder = remember { placeholder }
    )
    // [END android_compose_images_load_internet_coil]
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun GreetingPreview() {
    GithubExplorerTheme {
        HelloScreen(
            {}, TestViewPreview(), BrushPainter(Brush.verticalGradient(listOf(Color.Red, Color.Blue))
            ), PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_4_XL)
@Composable
fun LoadingPreview() {
    GithubExplorerTheme {
        Loading {}
    }
}

@Composable
fun TestViewPreview() {
    placeHolderOrDrawableRes(
        "https://avatars.githubusercontent.com/u/10316435?v=4",
        com.example.githubexplorer.R.drawable.ic_launcher_background
    )
}



/**
 * Returns a [Painter] that is either the one specified
 * in the url, or a default value specified in the placeholder parameter.
 * @param urlOrDrawableRes If it contains an integer, then use it
 * as a [DrawableRes] instead of the placeholder.
 * @param placeholder The [DrawableRes] to use if the
 * url is not an integer.
 * @return An initialised [Painter].
 */
@Composable
fun placeHolderOrDrawableRes(
    urlOrDrawableRes: String,
    @DrawableRes placeholder: Int): Painter {
    val drawableRes = urlOrDrawableRes.toIntOrNull()
    return when {
        drawableRes != null -> painterResource(id = drawableRes)
        else -> painterResource(id = placeholder)
    }
}
