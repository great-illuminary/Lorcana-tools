import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import eu.codlab.lorcana.blipya.home.App
import org.jetbrains.compose.resources.configureWebResources
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalJsExport::class, ExperimentalComposeUiApi::class)
@JsExport
fun main() {
    onWasmReady {
        CanvasBasedWindow(
            title = "Blipya's Lorcana Tool"
        ) {
            configureWebResources {
                resourcePathMapping { path ->
                    "/$path"
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                App(isSystemInDarkTheme())
            }
        }
    }
}
