package eu.codlab.lorcana.blipya.theme

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform

data class FontSizes(
    val menu: MenuSize = MenuSize(),
    val popup: PopupSize = PopupSize(),
    val actionBar: ActionBar = ActionBar(),
)

data class MenuSize(
    val title: TextUnit = 14.sp,
    val item: TextUnit = 14.sp,
    val switch: TextUnit = 12.sp
)

data class PopupSize(
    val title: TextUnit = 24.sp,
    val text: TextUnit = 16.sp,
    val button: TextUnit = 14.sp
)

data class ActionBar(
    val title: TextUnit = TextUnit.Unspecified
)

private val defaultFontSizes = FontSizes()

private val jvmFontSizes = FontSizes(
    menu = MenuSize(
        title = 12.sp,
        item = 12.sp,
        switch = 10.sp,
    ),
    popup = PopupSize(
        title = 20.sp,
        text = 16.sp,
        button = 12.sp
    ),
    actionBar = ActionBar(
        title = 18.sp
    )
)

fun createFontSizes(platform: Platform = currentPlatform) = when (platform) {
    Platform.ANDROID -> defaultFontSizes
    Platform.IOS -> defaultFontSizes
    Platform.JVM -> jvmFontSizes
    Platform.JS -> jvmFontSizes
    Platform.LINUX -> jvmFontSizes
    Platform.MACOS -> jvmFontSizes
    Platform.WINDOWS -> jvmFontSizes
}
