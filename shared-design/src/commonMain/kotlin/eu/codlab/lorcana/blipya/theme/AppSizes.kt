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

private val defaultLoginSizes = LoginSizes()
private val jvmLoginSizes = LoginSizes(
    height = 40.dp,
    fontSize = 16.sp
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

object AppSizes {
    val divider = 1.dp
    val paddings = Paddings()
    val corners = Corners()
    val login = createLoginSizes()
    val overflowMenu: OverflowMenuSizes = OverflowMenuSizes()
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
