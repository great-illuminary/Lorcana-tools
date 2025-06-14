package eu.codlab.lorcana.blipya.curve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_info_cost_title
import eu.codlab.blipya.res.curve_info_deck_related_info
import eu.codlab.blipya.res.curve_information_original
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.curve.ShowCurveInformation
import eu.codlab.lorcana.blipya.deck.curve.ShowCurveInformationGraph
import eu.codlab.lorcana.blipya.deck.expectedNumberOfColumns
import eu.codlab.lorcana.blipya.deck.expectedSpanForGraphTiles
import eu.codlab.lorcana.blipya.deck.expectedSpanForRegularTiles
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.viewmodel.rememberViewModel

@Composable
fun CurveInformation(
    modifier: Modifier
) {
    val model = rememberViewModel { CurveInformationModel() }
    val state by model.states.collectAsState()

    val columns = expectedNumberOfColumns()
    val expectedSpanForRegularTiles = expectedSpanForRegularTiles()
    val (curveGraph, curveInfo) = expectedSpanForGraphTiles()

    LazyVerticalGrid(
        modifier = modifier.imePadding(),
        contentPadding = PaddingValues(AppSizes.paddings.default),
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
    ) {
        item(span = { GridItemSpan(curveGraph) }) {
            ShowCurveInformationGraph(
                Modifier.fillMaxSize(),
                listOfNotNull(
                    state.calculatedDeckCurve?.let {
                        Triple(
                            Res.string.curve_information_original.localized(),
                            it,
                            AppColor.Blue
                        )
                    }
                )
            )
        }

        item(span = { GridItemSpan(curveInfo) }) {
            ShowCurveInformation(
                Modifier.fillMaxSize(),
                state.calculatedDeckCurve
            )
        }

        item(span = { GridItemSpan(columns) }) {
            TextNormal(
                text = Res.string.curve_info_deck_related_info.localized(),
                fontSize = LocalFontSizes.current.deckInfo.section,
                fontWeight = FontWeight.Bold
            )
        }

        item(span = { GridItemSpan(expectedSpanForRegularTiles) }) {
            ShowUninkableInput(Modifier.fillMaxSize(), model)
        }

        item(span = { GridItemSpan(columns) }) {
            TextNormal(
                text = Res.string.curve_info_cost_title.localized(),
                fontSize = LocalFontSizes.current.deckInfo.section,
                fontWeight = FontWeight.Bold
            )
        }

        items(state.curvesCost.keys.size) {
            ShowCurveInput(Modifier.fillMaxSize(), it, model)
        }
    }
}
