package eu.codlab.lorcana.blipya.decks

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.rememberColumns
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.PromptDialog
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.math.Deck

@Composable
fun DecksScreen(
    modifier: Modifier = Modifier,
    appModel: AppModel,
    onDeckSelected: (Deck) -> Unit
) {
    val state by appModel.states.collectAsState()
    val decks = state.decks

    val columns = rememberColumns()
    val color = defaultCardBackground()

    println("DecksScreen prompt ? ${state.showPromptNewDeck}")

    if (state.showPromptNewDeck) {
        PromptDialog(
            onDismiss = { appModel.showAddDeck(false) },
            onConfirm = { deckName ->
                appModel.addDeck(deckName) { deck ->
                    appModel.showAddDeck(false)
                    onDeckSelected(deck)
                }
            }
        )
    }

    if (decks.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextNormal("No decks for now")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(decks.size) { index ->
            val deck = decks[index]

            DefaultCard(
                columnModifier = Modifier.padding(12.dp),
                backgroundColor = color,
                onClick = { onDeckSelected(deck) }
            ) {
                TextNormal(
                    text = deck.name ?: "No Name",
                )
                TextNormal(
                    text = "x${deck.size} cards",
                )
            }
        }

    }
}

@Preview
@Composable
private fun DecksScreenPreview() {
    PreviewDarkLightColumn { modifier, _ ->
        DecksScreen(
            modifier,
            AppModel.fake()
        ) {
            // nothing
        }
    }
}
