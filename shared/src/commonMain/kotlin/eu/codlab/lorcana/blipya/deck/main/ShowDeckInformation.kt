package eu.codlab.lorcana.blipya.deck.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
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
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun ShowDeckInformation(
    fillMaxSize: Modifier,
    model: DeckConfigurationModel
) {
    val color = defaultCardBackground()
    val maxWidth = 100.dp
    val state by model.states.collectAsState()
    var name by remember { mutableStateOf(TextFieldValue(state.name)) }

    DefaultCard(
        modifier = fillMaxSize,
        backgroundColor = color
    ) {
        Column(
            Modifier.padding(AppSizes.paddings.default).background(color),
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
