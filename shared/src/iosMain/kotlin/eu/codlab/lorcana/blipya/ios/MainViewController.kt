package eu.codlab.lorcana.blipya.ios

import androidx.compose.foundation.isSystemInDarkTheme
import eu.codlab.lorcana.blipya.home.App
import eu.codlab.safearea.views.WindowInsetsUIViewController

@Suppress("FunctionNaming")
fun MainViewController() = WindowInsetsUIViewController {
    App(isDarkTheme = isSystemInDarkTheme())
}
