package eu.codlab.lorcana.blipya.cards.listing

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.cards_list_no_result
import eu.codlab.blipya.res.hint_research
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.expectedNumberOfColumns
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
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

    Column {
        Header(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            model = model
        )

        LazyVerticalGrid(
            modifier = modifier.imePadding(),
            contentPadding = PaddingValues(AppSizes.paddings.default),
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
            horizontalArrangement = Arrangement.spacedBy(AppSizes.paddings.default),
        ) {
            showEmptySectionIfRequired(
                Res.string.cards_list_no_result,
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

@Composable
private fun Header(
    modifier: Modifier,
    model: CardsListingModel
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val state by model.states.collectAsState()

    DefaultCard(
        modifier = modifier,
        backgroundColor = defaultCardBackground(),
        columnModifier = Modifier.fillMaxWidth().padding(6.dp)
    ) {
        CustomOutlinedEditText(
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                TextNormal(
                    modifier = Modifier.alpha(0.5f),
                    text = Res.string.hint_research.localized()
                )
            },
            value = text,
            onValueChanged = {
                text = it
                model.search(it.text)
            },
            isError = null != state.searchError
        )
    }
}

@Composable
@Preview
private fun PreviewCardsListing() {
    PreviewDarkLightColumn(frameType = WindowType.SMARTPHONE) { modifier, _ ->
        CardsListing(
            AppModel.fake(),
            modifier
        )
    }
}
