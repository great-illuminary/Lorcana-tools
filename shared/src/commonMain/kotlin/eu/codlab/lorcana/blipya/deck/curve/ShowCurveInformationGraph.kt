package eu.codlab.lorcana.blipya.deck.curve

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_information_keep_2
import eu.codlab.blipya.res.curve_information_keep_4
import eu.codlab.blipya.res.curve_information_original
import eu.codlab.blipya.res.curve_information_title
import eu.codlab.blipya.res.curve_information_title_graph
import eu.codlab.blipya.res.curve_information_turn
import eu.codlab.blipya.res.title_deck_info_no_info
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.DeckConfigurationModel
import eu.codlab.lorcana.blipya.deck.dreamborn.PromptForDreambornUrl
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.ShowLineChartCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Suppress("LongMethod")
@Composable
fun ShowCurveInformationGraph(
    modifier: Modifier,
    model: DeckConfigurationModel
) {
    val state by model.states.collectAsState()

    DefaultCard(
        modifier = modifier,
        backgroundColor = defaultCardBackground()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            TextNormal(
                text = Res.string.curve_information_title_graph.localized(),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))
            Divider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            val turn = Res.string.curve_information_turn.localized()

            if (null == state.calculateDeckCurve) {
                Column(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextNormal(
                        text = Res.string.title_deck_info_no_info.localized(),
                        fontSize = LocalFontSizes.current.deckInfo.textEmpty,
                        textAlign = TextAlign.Center
                    )
                }
            }

            state.calculateDeckCurve?.let { curveHolder ->
                ShowLineChartCard(
                    modifier = Modifier.fillMaxSize(),
                    xAxisTitles = List(curveHolder.original.turnsInfo.size) { index -> "$turn ${index + 1}" },
                    title = "",
                    values = listOf(
                        Triple(
                            Res.string.curve_information_original.localized(),
                            curveHolder.original.turnsInfo.map { it.probability.toFloat() },
                            AppColor.Blue
                        ),
                        Triple(
                            Res.string.curve_information_keep_2.localized(),
                            curveHolder.withKeeping2OfEach.turnsInfo.map { it.probability.toFloat() },
                            AppColor.BackgroundDarkBlue
                        ),
                        Triple(
                            Res.string.curve_information_keep_4.localized(),
                            curveHolder.withKeeping4OfEach.turnsInfo.map { it.probability.toFloat() },
                            AppColor.Red
                        )
                    )
                )
            }
        }
    }
}
