package eu.codlab.lorcana.blipya.login

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.disconnect_text
import eu.codlab.blipya.res.disconnect_title
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.PromptDialog
import org.jetbrains.compose.resources.StringResource

@Composable
fun PromptForAction(
    appModel: AppModel,
    showPrompt: Boolean,
    titleResource: StringResource,
    textResource: StringResource,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    var prompt by remember { mutableStateOf(showPrompt) }

    LaunchedEffect(showPrompt) {
        prompt = showPrompt
    }

    if (!prompt) {
        return
    }

    PromptDialog(
        title = titleResource.localized(),
        text = textResource.localized(),
        onDismiss = {
            prompt = false
            onDismiss()
        },
        onConfirm = { deckName ->
            appModel.addDeck(deckName) { deck ->
                prompt = false
                onConfirm
            }
        }
    )
}

@Preview
@Composable
private fun PromptForActionPreview() {
    PreviewDarkLightColumn { _, _ ->
        PromptForAction(
            AppModel.fake(),
            true,
            titleResource = Res.string.disconnect_title,
            textResource = Res.string.disconnect_text,
            onConfirm = { /* nothing */ },
            onDismiss = { /* nothing */ }
        )
    }
}
