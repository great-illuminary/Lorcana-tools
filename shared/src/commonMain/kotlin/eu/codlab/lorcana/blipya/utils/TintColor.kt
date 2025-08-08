package eu.codlab.lorcana.blipya.utils

import androidx.compose.runtime.Composable
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.lorcana.blipya.theme.AppColor

@Composable
fun tintColor() = if (LocalDarkTheme.current) {
    AppColor.White
} else {
    AppColor.Black
}
