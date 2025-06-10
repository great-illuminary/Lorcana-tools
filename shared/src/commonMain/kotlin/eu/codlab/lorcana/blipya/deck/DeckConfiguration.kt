package eu.codlab.lorcana.blipya.deck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.deck_section_empty_deck
import eu.codlab.blipya.res.deck_section_empty_draw
import eu.codlab.blipya.res.deck_section_empty_mulligan
import eu.codlab.blipya.res.title_scenario_deck_dreamborn
import eu.codlab.blipya.res.title_scenario_draw
import eu.codlab.blipya.res.title_scenario_mulligan
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.card.ShowCard
import eu.codlab.lorcana.blipya.deck.curve.ShowCurveInformation
import eu.codlab.lorcana.blipya.deck.dreamborn.ShowDreambornInformation
import eu.codlab.lorcana.blipya.deck.main.ShowDeckInformation
import eu.codlab.lorcana.blipya.deck.mulligan.show.ShowMulligan
import eu.codlab.lorcana.blipya.deck.scenario.show.ShowScenario
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.math.Deck
import eu.codlab.viewmodel.rememberViewModel
import korlibs.io.util.UUID
import org.jetbrains.compose.resources.StringResource

@Suppress("LongMethod")
@Composable
fun DeckConfiguration(
    app: AppModel,
    deck: DeckModel,
    modifier: Modifier
) {
    val model = rememberViewModel { DeckConfigurationModel(app, deck) }
    val state by model.states.collectAsState()

    LaunchedEffect(deck) {
        model.changeDeck(deck)

        app.setActiveDeck(model)
    }

    val color = defaultCardBackground()
    val maxWidth = 100.dp
    val numberOfColumnsForMainItem = 2
    val columns = expectedNumberOfColumns()

    LazyVerticalGrid(
        modifier = modifier.imePadding(),
        contentPadding = PaddingValues(AppSizes.paddings.default),
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
    ) {
        item(span = { GridItemSpan(numberOfColumnsForMainItem) }) {
            ShowDeckInformation(
                Modifier.fillMaxSize(),
                model
            )
        }

        item {
            ShowDreambornInformation(
                Modifier.fillMaxSize(),
                model
            )
        }

        item {
            ShowCurveInformation(
                Modifier.fillMaxSize(),
                model
            )
        }

        item(span = { GridItemSpan(columns) }) {
            TextNormal(
                text = Res.string.title_scenario_mulligan.localized(),
                fontSize = LocalFontSizes.current.deckInfo.section,
                fontWeight = FontWeight.Bold
            )
        }

        ShowEmptySectionIfRequired(
            Res.string.deck_section_empty_mulligan,
            columns,
            state.mulligans.isEmpty()
        )

        items(state.mulligans.size) { index ->
            val holder = state.mulligans[index]

            DefaultCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = color
            ) {
                Column(
                    Modifier.padding(AppSizes.paddings.default).background(color),
                    verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default)
                ) {
                    ShowMulligan(Modifier.fillMaxWidth(), app, model, state.deck, holder)
                }
            }
        }

        item(span = { GridItemSpan(columns) }) {
            TextNormal(
                text = Res.string.title_scenario_draw.localized(),
                fontSize = LocalFontSizes.current.deckInfo.section,
                fontWeight = FontWeight.Bold
            )
        }

        ShowEmptySectionIfRequired(
            Res.string.deck_section_empty_draw,
            columns,
            state.scenarii.isEmpty()
        )

        items(state.scenarii.size) { index ->
            val holder = state.scenarii[index]

            DefaultCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = color
            ) {
                Column(
                    Modifier.padding(AppSizes.paddings.default).background(color),
                    verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default)
                ) {
                    ShowScenario(Modifier.fillMaxWidth(), app, model, state.deck, holder)
                }
            }
        }

        item(span = { GridItemSpan(columns) }) {
            TextNormal(
                text = Res.string.title_scenario_deck_dreamborn.localized(),
                fontSize = LocalFontSizes.current.deckInfo.section,
                fontWeight = FontWeight.Bold
            )
        }

        ShowEmptySectionIfRequired(
            Res.string.deck_section_empty_deck,
            columns,
            show = null != state.deckContent
        )

        state.deckContent?.let {
            listOf(
                it.characters,
                it.actions,
                it.songs,
                it.objects,
                it.locations
            ).forEach { list ->
                items(list.size) { index ->
                    ShowCard(
                        modifier = Modifier.fillMaxWidth(),
                        deck = state.deck.deck,
                        virtualCardNumber = list[index]
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.ShowEmptySectionIfRequired(
    text: StringResource,
    columns: Int,
    show: Boolean
) {
    if (show) {
        item(span = { GridItemSpan(columns) }) {
            Column(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextNormal(
                    text = text.localized(),
                    fontSize = LocalFontSizes.current.deckInfo.textEmpty,
                )
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun expectedNumberOfColumns(): Int {
    val columnsForReducedScreens = 2
    val columnsForExpandedScreens = 5

    // extract the required number of columns and the specific case where we will have 2 in a row
    // represents the expected number of columns AND the "span" of the first one
    return when (LocalFrame.current) {
        WindowType.SMARTPHONE_TINY -> columnsForReducedScreens
        WindowType.SMARTPHONE -> columnsForReducedScreens
        WindowType.PHABLET -> 3
        WindowType.TABLET -> columnsForExpandedScreens
    }
}

@Composable
@Preview
private fun PreviewDeckConfiguration() {
    PreviewDarkLightColumn(frameType = WindowType.SMARTPHONE) { modifier, _ ->
        DeckConfiguration(
            AppModel.fake(),
            DeckModel(
                Deck(UUID.randomUUID().toString(), "deck", 0, 0)
            ),
            modifier
        )
    }
}
