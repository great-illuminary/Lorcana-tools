package eu.codlab.lorcana.blipya.home.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.drawer.normal.DrawerContentNormal
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onMenuItemSelected: (title: String, navigateTo: NavigateTo) -> Unit
) {
    DrawerContentNormal(
        modifier,
        onMenuItemSelected = onMenuItemSelected
    )
}

@HotPreview(widthDp = 400, heightDp = 800, darkMode = true)
@HotPreview(widthDp = 400, heightDp = 800, darkMode = false)
@Composable
private fun PreviewDrawerContent() {
    HotPreviewApp {
        DrawerContent { _, _ ->
            // nothing}
        }
    }
}
