package eu.codlab.lorcana.blipya.deck.curve

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.deck.dreamborn.PromptForDreambornUrl
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Suppress("LongMethod")
@Composable
fun ShowCurveInformation(
    modifier: Modifier,
    model: DeckConfigurationModel
) {
    val state by model.states.collectAsState()
    var prompt by remember { mutableStateOf(false) }

    val tintColor = if (LocalDarkTheme.current) {
        AppColor.White
    } else {
        AppColor.Black
    }

    PromptForDreambornUrl(
        showPrompt = prompt,
        onDismiss = {
            prompt = false
        },
        onConfirm = {
            prompt = false
            model.loadDreamborn(it)
        }
    )

    DefaultCard(
        modifier = modifier,
        backgroundColor = defaultCardBackground()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            TextNormal(
                text = "CurveInformation",
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))
            Divider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            state.calculateDeckCurve?.let {
                TextNormal(
                    text = "Original ${it.original.turnsInfo.map { it.probability }}"
                )

                TextNormal(
                    text = "With2 ${it.withKeeping2OfEach.turnsInfo.map { it.probability }}"
                )

                TextNormal(
                    text = "With4 ${it.withKeeping4OfEach.turnsInfo.map { it.probability }}"
                )
            }
        }
    }
}
