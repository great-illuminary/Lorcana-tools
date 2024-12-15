package eu.codlab.lorcana.blipya.decks

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.widgets.PromptDialog
import eu.codlab.lorcana.math.Deck

@Composable
fun PromptForNewDeck(
    appModel: AppModel,
    showPrompt: Boolean,
    onDismiss: () -> Unit,
    onDeckSelected: (Deck) -> Unit,
) {
    var prompt by remember { mutableStateOf(showPrompt) }

    LaunchedEffect(showPrompt) {
        prompt = showPrompt
    }

    if (!prompt) {
        return
    }

    PromptDialog(
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
            onDeckSelected = { /** nothing */ },
            onDismiss = { /** nothing */ }
        )
    }
}
