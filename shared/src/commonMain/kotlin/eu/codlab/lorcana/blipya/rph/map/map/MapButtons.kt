package eu.codlab.lorcana.blipya.rph.map.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.theme.LocalThemeEnvironment
import eu.codlab.compose.theme.ThemeEnvironment
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.icons.FixedGps
import eu.codlab.lorcana.blipya.icons.ZoomIn
import eu.codlab.lorcana.blipya.icons.ZoomOut
import eu.codlab.lorcana.blipya.rph.map.RphMapModel
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform

@Composable
fun MapButtons(
    modifier: Modifier,
    model: RphMapModel
) {
    val color = defaultCardBackground()
    val theme = LocalThemeEnvironment.current

    Column(
        modifier = modifier,
        // backgroundColor = color,
        //columnModifier = Modifier.padding(8.dp)
    ) {
        if (LocalFrame.current == WindowType.TABLET || LocalFrame.current == WindowType.PHABLET) {
            Row(
                Modifier.height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShowActions(model)
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShowActions(model)
            }
        }
    }
}

@Composable
private fun Button(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    val colors = LocalThemeEnvironment .current
    val color = defaultCardBackground()

    val corner = RoundedCornerShape(50)

    Column(Modifier.clip(corner)) {
        DefaultCard(
            onClick = onClick,
            backgroundColor = color,
            shape = corner,
            columnModifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = if(LocalDarkTheme.current) {
                    AppColor.WhiteCream
                }else {
                    AppColor.GrayDark
                }
            )
        }
    }
}

@Composable
private fun ShowActions(
    model: RphMapModel
) {
    val mobile = currentPlatform == Platform.IOS || currentPlatform == Platform.ANDROID

    if (!mobile) {
        Button(
            imageVector = Icons.Rounded.ZoomIn,
            contentDescription = "Zoom In",
            onClick = { model.zoomIn() }
        )
        Button(
            imageVector = Icons.Rounded.ZoomOut,
            contentDescription = "Zoom Out",
            onClick = { model.zoomOut() }
        )
    }
    Button(
        imageVector = Icons.Rounded.FixedGps,
        contentDescription = "GPS",
        onClick = { model.moveToOrigin() }
    )
}

@Composable
@HotPreview(widthDp = 250, heightDp = 50, darkMode = true)
@HotPreview(widthDp = 250, heightDp = 50, darkMode = false)
private fun MapButtonsPreview() {
    HotPreviewApp(Modifier.fillMaxSize(), isDarkTheme = isSystemInDarkTheme()) {
        MapButtons(
            Modifier.fillMaxSize(),
            RphMapModel()
        )
    }
}
