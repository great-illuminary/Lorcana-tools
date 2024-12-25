package eu.codlab.lorcana.blipya.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.cancel
import eu.codlab.blipya.res.validate
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.localized

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var deckName by remember { mutableStateOf(TextFieldValue("")) }
    val dark = LocalDarkTheme.current
    val color = if (dark) {
        AppColor.BackgroundBlue
    } else {
        AppColor.White
    }

    val textColor = if (dark) {
        AppColor.White
    } else {
        AppColor.Black
    }
    val reverseTextColor = if (dark) {
        AppColor.BackgroundBlue
    } else {
        AppColor.Black
    }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        DefaultCard(
            backgroundColor = color,
            modifier = Modifier
                .widthIn(100.dp, 200.dp)
                .heightIn(50.dp, 200.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = deckName,
                    onValueChange = {
                        deckName = it
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = textColor,
                        unfocusedBorderColor = textColor,
                        unfocusedLabelColor = textColor
                    ),
                    label = {
                        TextNormal(
                            color = textColor,
                            text = title
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        elevation = ButtonDefaults.elevation(0.dp),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        TextNormal(
                            color = reverseTextColor,
                            text = Res.string.cancel.localized()
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    OutlinedButton(
                        enabled = deckName.text.isNotBlank(),
                        onClick = {
                            onConfirm(deckName.text)
                        }
                    ) {
                        TextNormal(
                            color = reverseTextColor,
                            text = Res.string.validate.localized()
                        )
                    }
                }
            }
        }
    }
}
