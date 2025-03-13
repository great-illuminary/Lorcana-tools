package eu.codlab.lorcana.blipya.utils

import androidx.compose.ui.text.input.TextFieldValue

@Suppress("SwallowedException", "TooGenericExceptionCaught")
fun TextFieldValue.asLongOrNull() = try {
    text.toLong()
} catch (err: Throwable) {
    null
}
