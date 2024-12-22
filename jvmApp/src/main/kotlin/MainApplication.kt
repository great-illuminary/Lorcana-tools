import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import eu.codlab.lorcana.blipya.home.App
import moe.tlaster.precompose.PreComposeApp
import java.awt.Dimension
import kotlin.system.exitProcess

fun main() = application {
    Window(
        onCloseRequest = {
            exitProcess(0)
        },
        title = "Blipya",
        state = WindowState(
            size = DpSize(700.dp, 500.dp)
        )
    ) {
        window.minimumSize = Dimension(700, 500)
        PreComposeApp {
            App(isDarkTheme = isSystemInDarkTheme())
        }
    }
}
