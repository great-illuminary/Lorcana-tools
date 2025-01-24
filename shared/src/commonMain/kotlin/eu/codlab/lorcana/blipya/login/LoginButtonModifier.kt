package eu.codlab.lorcana.blipya.login

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.theme.AppSizes

fun Modifier.loginButton() = height(AppSizes.login.height)
    .widthIn(
        AppSizes.login.minWidth,
        AppSizes.login.maxWidth,
    )