package eu.codlab.lorcana.blipya.home.drawer.normal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.decks_title
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.spacers.TopSpacer
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.drawer.DrawerItem
import eu.codlab.lorcana.blipya.home.drawer.DrawerTitle
import eu.codlab.lorcana.blipya.home.drawer.systemBackground
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.theme.AppColor
import org.jetbrains.compose.resources.stringResource


@Suppress("LongMethod", "UnusedPrivateMember")
@Composable
fun DrawerContentNormal(
    modifier: Modifier = Modifier,
    currentRoute: NavigateTo? = null,
    tiny: Boolean = false,
    onMenuItemSelected: (title: String, navigateTo: NavigateTo) -> Unit
) {
    val dark = LocalDarkTheme.current

    println("showing tiny ? $tiny")

    Column(modifier = modifier) {
        Card(
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (dark) {
                        AppColor.BackgroundBlue
                    } else {
                        AppColor.White
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .systemBackground()
                    .verticalScroll(rememberScrollState())
                    .background(
                        color = if (dark) {
                            AppColor.BackgroundBlue
                        } else {
                            AppColor.White
                        }
                    )
            ) {
                TopSpacer()

                if (!tiny) {
                    DrawerTitle(text = "nothing for now")
                } else {
                    // nothing
                }

                DrawerItem(
                    text = stringResource(Res.string.decks_title),
                    currentRoute = currentRoute,
                    key = NavigateTo.Main(),
                    image = Icons.Rounded.Warning,
                    onClick = onMenuItemSelected,
                    tiny = tiny
                )
            }
        }
    }
}

@Composable
private fun PreviewDrawerContent() {
    HotPreviewApp {
        DrawerContentNormal(
            tiny = false
        ) { _, _ ->
            // nothing}
        }
    }
}
