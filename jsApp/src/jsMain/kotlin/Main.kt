import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.home.App
import moe.tlaster.precompose.preComposeWindow
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
    onWasmReady {
        preComposeWindow(
            title = "Blipya's Lorcana Tool"
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                App(isSystemInDarkTheme())
            }
        }
    }
}
