package eu.codlab.lorcana.blipya.deck.curve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_information_average_cost
import eu.codlab.blipya.res.curve_information_inkables_in_deck
import eu.codlab.blipya.res.curve_information_max_ink
import eu.codlab.blipya.res.curve_information_probability
import eu.codlab.blipya.res.curve_information_title
import eu.codlab.blipya.res.curve_information_uninkables_in_deck
import eu.codlab.blipya.res.title_deck_info_no_info
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.edit.showProbability
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.math.CurveInfo

@Suppress("LongMethod")
@Composable
fun ShowCurveInformation(
    modifier: Modifier,
    curveInformation: CurveInfo?
) {
    DefaultCard(
        modifier = modifier,
        backgroundColor = defaultCardBackground()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            TextNormal(
                text = Res.string.curve_information_title.localized(),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))
            Divider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            if (null == curveInformation) {
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

            curveInformation?.let { curveHolder ->
                Column(modifier) {
                    Row(modifier) {
                        TextNormal(
                            "${Res.string.curve_information_probability.localized()} : " +
                                    curveHolder.probability.showProbability()
                        )
                    }

                    Row(modifier) {
                        TextNormal("${Res.string.curve_information_max_ink.localized()} : ${curveHolder.maxInk}")
                    }

                    Row(modifier) {
                        TextNormal(
                            "${Res.string.curve_information_average_cost.localized()} :" +
                                    curveHolder.averageCost.showProbability()
                        )
                    }

                    Row(modifier) {
                        TextNormal("${Res.string.curve_information_inkables_in_deck.localized()} : ${curveHolder.inkablesInDeck}")
                    }

                    Row(modifier) {
                        TextNormal("${Res.string.curve_information_uninkables_in_deck.localized()} : ${curveHolder.uninkablesInDeck}")
                    }
                }
            }
        }
    }
}
