package eu.codlab.lorcana.blipya.utils

import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.asLongOrNull() = try {
    text.toLong()
} catch (err: Throwable) {
    null
}