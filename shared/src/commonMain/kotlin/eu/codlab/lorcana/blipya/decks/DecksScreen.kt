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
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.decks_cards
import eu.codlab.blipya.res.decks_no_decks
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.utils.rememberColumns
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun DecksScreen(
    modifier: Modifier = Modifier,
    appModel: AppModel,
    onDeckSelected: (DeckModel) -> Unit
) {
    val state by appModel.states.collectAsState()
    val decks = state.decks

    val columns = rememberColumns()
    val color = defaultCardBackground()

    if (decks.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextNormal(Res.string.decks_no_decks.localized())
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(AppSizes.paddings.default),
        verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default)
    ) {
        items(decks.size) { index ->
            val deck = decks[index]

            DefaultCard(
                columnModifier = Modifier.padding(AppSizes.paddings.card),
                backgroundColor = color,
                onClick = { onDeckSelected(deck) }
            ) {
                TextNormal(
                    text = deck.name,
                )
                TextNormal(
                    text = "x${deck.size} ${Res.string.decks_cards.localized()}",
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
