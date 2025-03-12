package eu.codlab.lorcana.blipya.deck.dreamborn

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.dreamborn_url_or_id_text
import eu.codlab.blipya.res.dreamborn_url_or_id_title
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.PromptDialog

@Composable
fun PromptForDreambornUrl(
    showPrompt: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var prompt by remember { mutableStateOf(showPrompt) }

    LaunchedEffect(showPrompt) {
        prompt = showPrompt
    }

    if (!prompt) {
        return
    }

    PromptDialog(
        title = Res.string.dreamborn_url_or_id_title.localized(),
        text = Res.string.dreamborn_url_or_id_text.localized(),
        onDismiss = {
            prompt = false
            onDismiss()
        },
        onConfirm = {
            prompt = false
            onConfirm(it)
        }
    )
}

@Preview
@Composable
private fun PromptForNewDeckPreview() {
    PreviewDarkLightColumn { _, _ ->
        PromptForDreambornUrl(
            true,
            onConfirm = { /* nothing */ },
            onDismiss = { /* nothing */ }
        )
    }
}
