import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import eu.codlab.lorcana.blipya.home.App
import moe.tlaster.precompose.PreComposeApp
import kotlin.system.exitProcess

val WindowSize = DpSize(700.dp, 500.dp)

fun main() = application {
    Window(
        onCloseRequest = {
            exitProcess(0)
        },
        title = "Blip's",
        state = WindowState(size = WindowSize)
    ) {
        PreComposeApp {
            App(isDarkTheme = isSystemInDarkTheme())
        }
    }
}
