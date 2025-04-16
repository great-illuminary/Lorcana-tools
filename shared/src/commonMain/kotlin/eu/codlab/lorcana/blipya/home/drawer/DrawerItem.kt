package eu.codlab.lorcana.blipya.home.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.CustomOutlinedButton
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.navigate.NavigateTo
import eu.codlab.lorcana.blipya.icons.Folder
import eu.codlab.lorcana.blipya.local.LocalFontSizes

@Suppress("MagicNumber")
@Composable
fun DrawerSeparator(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(8.dp)
            .fillMaxWidth()
            .systemBackground()
            .padding(start = 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .height(1.dp)
                .systemBackground(),
            color = colorDimmed()
        )
    }
}

@Suppress("MagicNumber")
@Composable
fun DrawerTitle(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean = false
) {
    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .systemBackground(selected = selected)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextNormal(
            fontSize = LocalFontSizes.current.menu.title,
            text = text,
            fontFamily = FontFamily.SansSerif,
            color = colorDimmed()
        )
    }
}

@Suppress("MagicNumber")
@Composable
fun DrawerItem(
    modifier: Modifier = Modifier,
    text: String,
    key: NavigateTo,
    currentRoute: NavigateTo? = null,
    image: ImageVector,
    tiny: Boolean = false,
    onClick: (title: String, key: NavigateTo) -> Unit
) {
    val selected = currentRoute == key

    if (tiny) {
        Row(
            modifier = modifier
                .height(32.dp)
                .fillMaxWidth()
                .systemBackground(selected = selected)
                .padding(start = 8.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { onClick(text, key) }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = image,
                contentDescription = text,
                colorFilter = ColorFilter.tint(color(selected))
            )
        }
        return
    }

    Row(
        modifier = modifier
            .height(32.dp)
            .fillMaxWidth()
            .systemBackground(selected = selected)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = ripple(),
                onClick = { onClick(text, key) }
            )
            .padding(0.dp)
            .padding(start = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            imageVector = image,
            contentDescription = text,
            colorFilter = ColorFilter.tint(color(selected))
        )

        TextNormal(
            fontSize = LocalFontSizes.current.menu.item,
            text = text,
            fontFamily = FontFamily.Default,
            color = color(selected)
        )
    }
}

@Suppress("MagicNumber")
@Composable
fun DrawerButton(
    modifier: Modifier = Modifier,
    text: String,
    image: ImageVector,
    tiny: Boolean = false,
    onClick: () -> Unit
) {
    if (tiny) {
        Row(
            modifier = modifier
                .height(32.dp)
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onClick
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = image,
                contentDescription = text,
                colorFilter = ColorFilter.tint(color(false))
            )
        }
        return
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomOutlinedButton(
            modifier = Modifier.padding(8.dp),
            onClick = onClick
        ) {
            TextNormal(
                fontSize = LocalFontSizes.current.menu.switch,
                text = text
            )
        }
    }
}

@Suppress("MagicNumber")
@Composable
fun color(selected: Boolean = false): Color {
    if (selected) {
        return if (LocalDarkTheme.current) {
            Color(0xFFFFFFFF)
        } else {
            Color(0xFF3A3A3A)
        }
    }

    return if (LocalDarkTheme.current) {
        Color(0xFFFFFFFF)
    } else {
        Color(0xFF3A3A3A)
    }
}

@Suppress("MagicNumber")
@Composable
fun colorDimmed(): Color {
    return if (LocalDarkTheme.current) {
        Color(0xaaFFFFFF)
    } else {
        Color(0xaa3A3A3A)
    }
}

@Suppress("MagicNumber")
@Composable
fun Modifier.systemBackground(selected: Boolean = false): Modifier {
    return if (selected) {
        this.background(
            if (LocalDarkTheme.current) {
                Color(0xFF242424)
            } else {
                Color(0xFF9E9E9E)
            }
        )
    } else {
        this
    }
}

@HotPreview(widthDp = 200, heightDp = 50, darkMode = true)
@HotPreview(widthDp = 200, heightDp = 50, darkMode = false)
@Composable
private fun DrawerItemPreviewDark() {
    HotPreviewApp {
        Column(modifier = Modifier.fillMaxWidth()) {
            DrawerTitle(text = "Title")
            DrawerSeparator()
            DrawerItem(
                text = "Item",
                key = NavigateTo.Main(),
                image = Icons.Rounded.Folder
            ) { _, _ ->
                // nothing
            }
            DrawerItem(
                text = "Item",
                currentRoute = NavigateTo.Main(),
                key = NavigateTo.Main(),
                image = Icons.Rounded.Folder
            ) { _, _ ->
                // nothing
            }

            DrawerItem(
                text = "Item",
                currentRoute = NavigateTo.Main(),
                key = NavigateTo.Main(),
                tiny = true,
                image = Icons.Rounded.Folder
            ) { _, _ ->
                // nothing
            }

            DrawerButton(
                image = Icons.Default.Refresh,
                text = "Test for button only"
            ) {
                // nothing
            }
        }
    }
}
