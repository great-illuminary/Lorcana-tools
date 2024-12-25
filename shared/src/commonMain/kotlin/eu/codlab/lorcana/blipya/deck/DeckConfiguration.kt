package eu.codlab.lorcana.blipya.deck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.deck_configuration_deck_name
import eu.codlab.blipya.res.deck_configuration_deck_size
import eu.codlab.blipya.res.deck_configuration_hand_size
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.dreamborn.ShowDreambornInformation
import eu.codlab.lorcana.blipya.deck.scenario.ShowScenario
import eu.codlab.lorcana.blipya.home.AppModel
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

@Composable
fun DeckConfiguration(
    app: AppModel,
    deck: DeckModel,
    modifier: Modifier
) {
    val model = rememberViewModel { DeckConfigurationModel(app, deck) }
    val state by model.states.collectAsState()
    var name by remember { mutableStateOf(TextFieldValue(state.name)) }

    LaunchedEffect(deck) {
        model.changeDeck(deck)

        app.setActiveDeck(model)
    }

    val color = defaultCardBackground()
    val maxWidth = 100.dp

    // extract the required number of columns and the specific case where we will have 2 in a row
    // represents the expected number of columns AND the "span" of the first one
    val (columns, firstSpanColumns) = when (LocalFrame.current) {
        WindowType.SMARTPHONE_TINY -> 2 to 2
        WindowType.SMARTPHONE -> 2 to 2
        WindowType.PHABLET -> 3 to 2
        WindowType.TABLET -> 3 to 2
    }

    LazyVerticalGrid(
        modifier = modifier.imePadding(),
        contentPadding = PaddingValues(AppSizes.paddings.default),
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
    ) {
        item(span = { GridItemSpan(firstSpanColumns) }) {
            DefaultCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = color
            ) {
                Column(
                    Modifier.padding(16.dp).background(color),
                    verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default)
                ) {
                    CustomOutlinedEditText(
                        modifier = Modifier.width(200.dp),
                        value = name,
                        onValueChanged = {
                            name = it
                            model.updateDeck(name.text)
                        },
                        label = {
                            TextNormal(Res.string.deck_configuration_deck_name.localized())
                        }
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default)
                    ) {
                        CustomOutlinedEditText(
                            modifier = Modifier.widthIn(0.dp, maxWidth),
                            value = state.deckSize,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            onValueChanged = {
                                model.updateDeckSize(it)
                            },
                            label = {
                                TextNormal(Res.string.deck_configuration_deck_size.localized())
                            }
                        )

                        CustomOutlinedEditText(
                            modifier = Modifier.widthIn(0.dp, maxWidth),
                            value = state.handSize,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            onValueChanged = {
                                model.updateHandSize(it)
                            },
                            label = {
                                TextNormal(Res.string.deck_configuration_hand_size.localized())
                            }
                        )
                    }
                }
            }
        }

        item {
            ShowDreambornInformation(
                Modifier.fillMaxSize(),
                model
            )
        }

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