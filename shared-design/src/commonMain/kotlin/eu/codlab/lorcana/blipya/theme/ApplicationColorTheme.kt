package eu.codlab.lorcana.blipya.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalApplicationColorTheme = compositionLocalOf<ApplicationColorTheme> {
    error("No ApplicationColorTheme, check LocalApplicationColorTheme")
}

data class ApplicationColorTheme(
    val fonts: ApplicationColorThemeFont,
    val background: Background
)

data class ApplicationColorThemeFont(
    val graySemiTransparent: Color,
    val regular: Color,
    val regularLighter: Color,
)

data class Background(
    val fragment: Color,
    val topAppBar: Color,
    val topAppBarText: Color,
    val card: CardBackground
)

data class CardBackground(
    val primarySelectedBackground: Color,
    val primaryRegularBackground: Color,
    val secondarySelectedBackground: Color,
    val secondaryRegularBackground: Color,
)

private val darkThemeColorEnvironment = ApplicationColorTheme(
    fonts = ApplicationColorThemeFont(
        regular = AppColor.WhiteCream,
        regularLighter = AppColor.GrayLight,
        graySemiTransparent = AppColor.GraySemiTransparentDark
    ),
    background = Background(
        fragment = AppColor.GrayExtraDark,
        topAppBar = AppColor.Black,
        topAppBarText = AppColor.White,
        card = CardBackground(
            primarySelectedBackground = AppColor.BackgroundBlue,
            primaryRegularBackground = AppColor.BackgroundLightBlue,
            secondarySelectedBackground = AppColor.BackgroundBlue,
            secondaryRegularBackground = AppColor.BackgroundLightBlue,
        )
    )
)

private val lightThemeColorEnvironment = ApplicationColorTheme(
    fonts = ApplicationColorThemeFont(
        regular = AppColor.GrayExtraDark,
        regularLighter = AppColor.GrayDark,
        graySemiTransparent = AppColor.GraySemiTransparentLight
    ),
    background = Background(
        fragment = AppColor.WhiteCream,
        topAppBar = AppColor.White,
        topAppBarText = AppColor.Black,
        card = CardBackground(
            primarySelectedBackground = AppColor.GrayLight,
            primaryRegularBackground = AppColor.WhiteCream,
            secondarySelectedBackground = AppColor.Gray,
            secondaryRegularBackground = AppColor.White,
        )
    )
)

@Suppress("MagicNumber")
@Composable

fun ApplicationColorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkThemeColorEnvironment
    } else {
        lightThemeColorEnvironment
    }

    CompositionLocalProvider(
        LocalApplicationColorTheme provides colors
    ) {
        content()
    }
}
