package eu.codlab.lorcana.blipya.ios

import androidx.compose.foundation.isSystemInDarkTheme
import eu.codlab.lorcana.blipya.home.App
import eu.codlab.safearea.views.WindowInsetsUIViewController
import moe.tlaster.precompose.PreComposeApp

@Suppress("FunctionNaming")
fun MainViewController() = WindowInsetsUIViewController {
    PreComposeApp {
        App(isDarkTheme = isSystemInDarkTheme())
    }
}
