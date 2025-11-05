package eu.codlab.lorcana.blipya.cards.listing

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.cards_found
import eu.codlab.blipya.res.cards_list_no_result
import eu.codlab.blipya.res.hint_research
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.CustomOutlinedEditText
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.expectedNumberOfColumns
import eu.codlab.lorcana.blipya.home.AppModel
import eu.codlab.lorcana.blipya.icons.Print
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.blipya.utils.Constants
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.viewmodel.rememberViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource

@Suppress("LongMethod")
@Composable
fun CardsListing(
    app: AppModel,
    modifier: Modifier
) {
    val model = rememberViewModel { CardsListingModel(app.states.value.lorcana!!) }
    val state by model.states.collectAsState()
    val cards = state.cards

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

            items(cards.size) { index ->
                ShowCard(
                    modifier = Modifier.fillMaxWidth(),
                    variant = cards[index].first
                )
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
    val uriHandler = LocalUriHandler.current

    val hasCards = state.cards.isNotEmpty()
    val tintColor = if (LocalDarkTheme.current) {
        AppColor.White
    } else {
        AppColor.Black
    }

    Row {
        DefaultCard(
            modifier = modifier,
            backgroundColor = defaultCardBackground(),
            columnModifier = Modifier.fillMaxWidth().padding(6.dp)
        ) {
            CustomOutlinedEditText(
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    TextNormal(
                        modifier = Modifier.alpha(Constants.halfAlpha),
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

            if (hasCards) {
                Row(
                    Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextNormal(
                        text = pluralStringResource(
                            Res.plurals.cards_found,
                            quantity = state.cards.size,
                            state.cards.size
                        ),
                        fontStyle = FontStyle.Italic
                    )

                    IconButton(
                        onClick = {
                            model.openProxy(uriHandler)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print",
                            tint = tintColor
                        )
                    }
                }
            }
        }
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
