package eu.codlab.lorcana.blipya.deck.card

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.deck.scenario.show.round
import eu.codlab.lorcana.blipya.dreamborn.CardNumber
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.LocalApp
import eu.codlab.lorcana.blipya.home.LocalIsPreview
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground
import eu.codlab.lorcana.cards.VariantRarity
import eu.codlab.lorcana.math.Deck
import eu.codlab.lorcana.raw.Ravensburger
import eu.codlab.lorcana.raw.SetDescription
import eu.codlab.lorcana.raw.VariantClassification
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

val ratio = 734.0f / 1024.0f

@Composable
fun ShowCard(
    modifier: Modifier,
    deck: Deck,
    number: CardNumber
) {
    // nothing for now
    val model = LocalApp.current
    val found = model.cardFromDreamborn(number.card) ?: return
    val (card, variant) = found

    val showCardModel by remember {
        mutableStateOf(
            ShowCardModelImpl(
                deck,
                number.number.toLong()
            )
        )
    }

    LaunchedEffect(number.number) {
        showCardModel.setCardNumber(number.number.toLong())
    }

    ShowCardInternal(modifier, showCardModel, variant)
}

@Composable
private fun ShowCardInternal(
    modifier: Modifier,
    model: ShowCardModel,
    variant: VariantClassification,
) {
    val cardModifier = modifier.aspectRatio(ratio)

    val state by model.states.collectAsState()
    val url = "https://api-lorcana.com/public/images/" +
            "${variant.set.name.lowercase()}/fr/${variant.id}.webp.jpeg"

    Box(cardModifier) {
        ShowCardFromUrl(cardModifier, url)

        listOf(
            Alignment.BottomEnd to "${state.probability.round(2)}%",
            Alignment.TopStart to "x${state.cardNumber}"
        ).map { (align, text) ->
            DefaultCard(
                modifier = Modifier.align(align),
                backgroundColor = defaultCardBackground()
            ) {
                TextNormal(
                    modifier = Modifier.padding(8.dp),
                    text = text
                )
            }
        }
    }
}

@Composable
private fun ShowCardFromUrl(
    modifier: Modifier,
    url: String
) {
    if (LocalIsPreview.current) {
        Column(modifier = modifier.background(Color.Blue)) { }
        return
    }

    KamelImage(
        modifier = modifier,
        resource = asyncPainterResource(url),
        contentDescription = "",
        onLoading = { _ ->
            // nothing
        },
        onFailure = { exception ->
            exception.printStackTrace()
            println("error ${exception.message}")
        }
    )
}

@HotPreview(widthDp = 200, heightDp = 200, darkMode = true)
@Preview
@Composable
fun ShowCardPreview() {
    val modifier = Modifier.fillMaxHeight()
    HotPreviewApp(modifier, isDarkTheme = true, isPreview = true) {
        val variantClassification = VariantClassification(
            SetDescription.TFC,
            1,
            "",
            Ravensburger(
                "",
                "",
                "",
                "",
                "",
                ""
            ),
            VariantRarity.D23
        )

        val model = ShowCardModelImpl.fake()

        ShowCardInternal(
            modifier,
            model,
            variantClassification
        )
    }
}
