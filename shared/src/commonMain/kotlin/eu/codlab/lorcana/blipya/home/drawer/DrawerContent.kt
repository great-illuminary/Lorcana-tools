package eu.codlab.lorcana.blipya.home.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.drawer.normal.DrawerContentNormal
import eu.codlab.navigation.NavigateTo
import eu.codlab.navigation.NavigateToStack
import eu.codlab.navigation.RouteParameterTo

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    tiny: Boolean = false,
    onMenuItemSelected: (title: String, navigateTo: NavigateTo) -> Unit
) {
    DrawerContentNormal(
        modifier,
        tiny = tiny,
        onMenuItemSelected = onMenuItemSelected
    )
}

@HotPreview(widthDp = 200, heightDp = 800, darkMode = true)
@HotPreview(widthDp = 200, heightDp = 800, darkMode = false)
@Composable
private fun PreviewDrawerContent() {
    HotPreviewApp {
        DrawerContent { _, _ ->
            // nothing}
        }
    }
}
