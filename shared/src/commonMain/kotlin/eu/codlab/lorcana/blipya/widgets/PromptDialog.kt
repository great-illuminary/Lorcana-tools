package eu.codlab.lorcana.blipya.widgets

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.cancel
import eu.codlab.blipya.res.validate
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.utils.localized

@Suppress("LongMethod")
@Composable
fun PromptDialog(
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var deckName by remember { mutableStateOf(TextFieldValue("")) }

    PopupConfirm(
        true,
        title = title,
        text = text,
        alternateText = {
            OutlinedTextField(
                value = deckName,
                onValueChange = {
                    deckName = it
                },
                label = {
                    TextNormal(
                        color = Color.Black,
                        text = title
                    )
                }
            )
        },
        onDismiss = {

        },
        actions = listOf(
            PopupConfirmAction(
                text = Res.string.cancel.localized(),
                isOutlined = false,
                onClick = { onDismiss() }
            ),
            PopupConfirmAction(
                text = Res.string.validate.localized(),
                isOutlined = true,
                onClick = {
                    onConfirm(deckName.text)
                }
            )
        )
    )
}
