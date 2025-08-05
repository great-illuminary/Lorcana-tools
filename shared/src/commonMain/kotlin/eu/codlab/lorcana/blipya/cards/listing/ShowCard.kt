package eu.codlab.lorcana.blipya.cards.listing

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.intl.Locale
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.theme.AppSizes
import eu.codlab.lorcana.cards.VariantRarity
import eu.codlab.lorcana.raw.Ravensburger
import eu.codlab.lorcana.raw.SetDescription
import eu.codlab.lorcana.raw.VariantClassification
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

const val Ratio = 734.0f / 1024.0f
val langs = listOf("fr", "en", "de", "it")

@Composable
fun ShowCard(
    modifier: Modifier,
    variant: VariantClassification,
) {
    val lang = langs.find { it == Locale.current.language.lowercase() } ?: "en"
    val cardModifier = modifier.aspectRatio(Ratio)

    val (url, fallback) = listOf(lang, "en").map {
        "https://api-lorcana.com/public/images/" +
                "${variant.set.name.lowercase()}/$it/${variant.id}${variant.suffix ?: ""}.webp.jpeg"
    }

    Box(cardModifier) {
        ShowCardFromUrl(cardModifier, url, fallback)
    }
}

@Composable
private fun ShowCardFromUrl(
    modifier: Modifier,
    url: String,
    fallback: String
) {
    var selectedUrl by remember { mutableStateOf(url) }

    Column(
        modifier = modifier.clip(shape = RoundedCornerShape(AppSizes.corners.lorcanaCards))
    ) {
        KamelImage(
            modifier = modifier,
            resource = asyncPainterResource(selectedUrl),
            contentDescription = "",
            onLoading = { _ ->
                // nothing
            },
            onFailure = { exception ->
                if (selectedUrl != fallback) {
                    selectedUrl = fallback
                }
                exception.printStackTrace()
                println("error loading $selectedUrl -> ${exception.message}")
            }
        )
    }
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
            dreamborn = "",
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

        ShowCard(
            modifier,

            variantClassification
        )
    }
}
