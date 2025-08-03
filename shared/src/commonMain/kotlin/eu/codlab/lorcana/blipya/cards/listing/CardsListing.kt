package eu.codlab.lorcana.blipya.cards.listing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.deck_section_empty_deck
import eu.codlab.blipya.res.title_scenario_deck_dreamborn
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.viewmodel.rememberViewModel
import org.jetbrains.compose.resources.StringResource

@Suppress("LongMethod")
@Composable
fun CardsListing(
    app: AppModel,
    modifier: Modifier
) {
    val model = rememberViewModel { CardsListingModel(app.states.value.lorcana!!) }
    val state by model.states.collectAsState()

    val columns = expectedNumberOfColumns()
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        CustomOutlinedEditText(
            value = text,
            onValueChanged = {
                println("new value $it")
                text = it
                model.search(it.text)
            },
            modifier = Modifier.padding(start = 10.dp),
        )

        LazyVerticalGrid(
            modifier = modifier.imePadding(),
            contentPadding = PaddingValues(AppSizes.paddings.default),
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
            horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        ) {
            item(span = { GridItemSpan(columns) }) {
                TextNormal(
                    text = Res.string.title_scenario_deck_dreamborn.localized(),
                    fontSize = LocalFontSizes.current.deckInfo.section,
                    fontWeight = FontWeight.Bold
                )
            }

            showEmptySectionIfRequired(
                Res.string.deck_section_empty_deck,
                columns,
                show = state.cards.isEmpty()
            )

            state.cards.let { cards ->
                items(cards.size) { index ->
                    ShowCard(
                        modifier = Modifier.fillMaxWidth(),
                        variant = cards[index].first
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.showEmptySectionIfRequired(
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
fun expectedNumberOfColumns(): Int {
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

@Suppress("MagicNumber")
@Composable
fun expectedSpanForGraphTiles(): Pair<Int, Int> {
    return when (LocalFrame.current) {
        WindowType.SMARTPHONE_TINY -> 2 to 2
        WindowType.SMARTPHONE -> 2 to 2
        WindowType.PHABLET -> 3 to 2
        WindowType.TABLET -> 3 to 2
    }
}

@Suppress("MagicNumber")
@Composable
fun expectedSpanForRegularTiles(): Int {
    val columnsForReducedScreens = 2

    return when (LocalFrame.current) {
        WindowType.SMARTPHONE_TINY -> columnsForReducedScreens
        WindowType.SMARTPHONE -> columnsForReducedScreens
        WindowType.PHABLET -> 2
        WindowType.TABLET -> 2
    }
}

@Composable
@HotPreview
private fun PreviewCardsListing() {
    PreviewDarkLightColumn(frameType = WindowType.SMARTPHONE) { modifier, _ ->
        CardsListing(
            AppModel.fake(),
            modifier
        )
    }
}
