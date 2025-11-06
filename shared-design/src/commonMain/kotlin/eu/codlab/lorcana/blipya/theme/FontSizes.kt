package eu.codlab.lorcana.blipya.theme

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import eu.codlab.platform.Platform
import eu.codlab.platform.currentPlatform

data class FontSizes(
    val menu: MenuSize = MenuSize(),
    val deckInfo: DeckInfo = DeckInfo(),
    val popup: PopupSize = PopupSize(),
    val actionBar: ActionBar = ActionBar(),
    val map: Map = Map(),
    val documentation: Documentation = Documentation()
)

data class Documentation(
    val title: TextUnit = 16.sp,
    val block: TextUnit = 12.sp,
)

data class DeckInfo(
    val section: TextUnit = 16.sp,
    val textEmpty: TextUnit = 12.sp,
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

data class Map(
    val activityLightning: TextUnit = 12.sp,
    val alertBannerSub: TextUnit = 18.sp,
    val alertBannerETA: TextUnit = 28.sp,
    val alertBannerTitle: TextUnit = 24.sp,
    val callOutBig: TextUnit = 22.sp,
    val callOutNormal: TextUnit = 12.sp,
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
    deckInfo = DeckInfo(
        section = 14.sp,
        textEmpty = 12.sp,
    ),
    popup = PopupSize(
        title = 20.sp,
        text = 16.sp,
        button = 12.sp
    ),
    actionBar = ActionBar(
        title = 18.sp
    ),
    documentation = Documentation(
        title = 18.sp,
        block = 16.sp
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
