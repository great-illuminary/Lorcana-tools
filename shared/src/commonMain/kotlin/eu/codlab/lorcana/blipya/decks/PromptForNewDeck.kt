package eu.codlab.lorcana.blipya.decks

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.deck_configuration_deck_name
import eu.codlab.blipya.res.deck_configuration_deck_name_text
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.PromptDialog

@Composable
fun PromptForNewDeck(
    appModel: AppModel,
    showPrompt: Boolean,
    onDismiss: () -> Unit,
    onDeckSelected: (DeckModel) -> Unit,
) {
    var prompt by remember { mutableStateOf(showPrompt) }

    LaunchedEffect(showPrompt) {
        prompt = showPrompt
    }

    if (!prompt) {
        return
    }

    PromptDialog(
        title = Res.string.deck_configuration_deck_name.localized(),
        text = Res.string.deck_configuration_deck_name_text.localized(),
        onDismiss = {
            println("dismiss")
            prompt = false
            onDismiss()
        },
        onConfirm = { deckName ->
            appModel.addDeck(deckName) { deck ->
                prompt = true
                appModel.showAddDeck(false)
                onDeckSelected(deck)
            }
        }
    )
}

@Preview
@Composable
private fun PromptForNewDeckPreview() {
    PreviewDarkLightColumn { _, _ ->
        PromptForNewDeck(
            AppModel.fake(),
            true,
            onDeckSelected = { /* nothing */ },
            onDismiss = { /* nothing */ }
        )
    }
}
