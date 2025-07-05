package eu.codlab.lorcana.blipya.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform

data class OverflowMenuSizes(
    val minWidth: Dp = 350.dp,
    val maxWidth: Dp = 450.dp
)

data class LoginSizes(
    val height: Dp = 44.dp,
    val fontSize: TextUnit = 15.sp,
    val minWidth: Dp = 350.dp,
    val maxWidth: Dp = 450.dp
)

private val internalPaddings = Paddings()

data class MapSize(
    val cardHorizontalPadding: Dp = internalPaddings.default,
    val cardVerticalPadding: Dp = internalPaddings.reduced,
    val cardImageAndTextHeight: Dp = 50.dp,
    val cardLightningLegendHeight: Dp = 40.dp
)

private val defaultLoginSizes = LoginSizes()
private val jvmLoginSizes = LoginSizes(
    height = 40.dp,
    fontSize = 16.sp
)

private val defaultMapSizes = MapSize()
private val jvmMapSizes = MapSize(
    cardImageAndTextHeight = 40.dp,
    cardLightningLegendHeight = 30.dp
)

fun createLoginSizes(platform: Platform = currentPlatform) = when (platform) {
    Platform.ANDROID -> defaultLoginSizes
    Platform.IOS -> defaultLoginSizes
    Platform.JVM -> jvmLoginSizes
    Platform.JS -> jvmLoginSizes
    Platform.LINUX -> jvmLoginSizes
    Platform.MACOS -> jvmLoginSizes
    Platform.WINDOWS -> jvmLoginSizes
}

fun createMapSizes(platform: Platform = currentPlatform) = when (platform) {
    Platform.ANDROID -> defaultMapSizes
    Platform.IOS -> defaultMapSizes
    else -> jvmMapSizes
}

object AppSizes {
    val divider = 1.dp
    val paddings = Paddings()
    val corners = Corners()
    val login = createLoginSizes()
    val overflowMenu: OverflowMenuSizes = OverflowMenuSizes()
    val map: MapSize = createMapSizes()
}

class Paddings {
    val card = 12.dp
    val default = 16.dp
    val reduced = 8.dp
    val reducedHalf = 4.dp
}

class Corners {
    val lorcanaCards = 8.dp
}
