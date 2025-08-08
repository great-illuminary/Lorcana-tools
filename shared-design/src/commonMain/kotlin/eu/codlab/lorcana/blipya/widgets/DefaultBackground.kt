package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.lorcana.blipya.theme.AppColor

fun Modifier.defaultBackground() = composed {
    /*this.background(
        if (LocalDarkTheme.current) {
            AppColor.BackgroundBlue
        } else {
            AppColor.GrayExtraLight // gray hyper light
        }
    )*/
    this
}

fun Modifier.defaultCardBackground() = composed {
    this.background(
        if (LocalDarkTheme.current) {
            AppColor.BackgroundBlue
        } else {
            AppColor.White
        }
    )
}

@Composable
fun defaultCardBackground(isSelected: Boolean = false) =
    if (isSelected) {
        if (LocalDarkTheme.current) {
            AppColor.BackgroundBlue
        } else {
            AppColor.GrayLight
        }
    } else if (LocalDarkTheme.current) {
        AppColor.BackgroundLightBlue
    } else {
        AppColor.WhiteCream
    }

@Composable
fun defaultSecondaryCardBackground(isSelected: Boolean = false) =
    if (isSelected) {
        if (LocalDarkTheme.current) {
            AppColor.BackgroundBlue
        } else {
            AppColor.Gray
        }
    } else if (LocalDarkTheme.current) {
        AppColor.BackgroundBlue
    } else {
        AppColor.White
    }
