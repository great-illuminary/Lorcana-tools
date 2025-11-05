package eu.codlab.lorcana.blipya.cards.documentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.documentation_artist_sample_1_explanation
import eu.codlab.blipya.res.documentation_artist_sample_1_query
import eu.codlab.blipya.res.edit
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.local.LocalFontSizes
import eu.codlab.lorcana.blipya.utils.LocalFrame
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.WindowType
import eu.codlab.lorcana.blipya.utils.localized
import org.jetbrains.compose.resources.StringResource

@Composable
fun DrawSection(
    modifier: Modifier,
    section: Section
) {
    val isColumn = when (LocalFrame.current) {
        WindowType.SMARTPHONE_TINY -> true
        WindowType.SMARTPHONE -> true
        WindowType.PHABLET -> false
        WindowType.TABLET -> false
    }

    @Composable
    fun Container(content: @Composable (Modifier) -> Unit) = if (isColumn) {
        Column(modifier) { content(Modifier.fillMaxWidth()) }
    } else {
        Row(modifier) { content(Modifier.weight(1.0f)) }
    }

    Container { modifier ->
        DrawSectionBlock(
            modifier,
            title = section.title,
            block = section.block
        )

        Column(modifier) {
            section.samples.forEach {
                DrawSample(
                    Modifier.fillMaxWidth(),
                    it
                )
            }
        }
    }
}

@Composable
private fun DrawSectionBlock(
    modifier: Modifier,
    title: StringResource,
    block: StringResource
) {
    val fonts = LocalFontSizes.current
    Column(modifier) {
        TextNormal(
            fontSize = fonts.documentation.title,
            text = title.localized(),
        )

        TextNormal(
            fontSize = fonts.documentation.block,
            text = block.localized(),
        )
    }
}

@Preview
@Composable
private fun DrawSectionPreview() {
    PreviewDarkLightColumn { modifier, isDark ->
        DrawSection(
            Modifier.fillMaxSize(),
            Section(
                title = Res.string.edit,
                block = Res.string.edit,
                samples = listOf(
                    Sample(
                        query = Res.string.documentation_artist_sample_1_query,
                        explanation = Res.string.documentation_artist_sample_1_explanation,
                    )
                )
            )
        )
    }
}