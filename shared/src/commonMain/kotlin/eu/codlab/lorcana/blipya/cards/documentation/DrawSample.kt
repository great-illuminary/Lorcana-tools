package eu.codlab.lorcana.blipya.cards.documentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.documentation_artist_sample_1_explanation
import eu.codlab.blipya.res.documentation_artist_sample_1_query
import eu.codlab.compose.widgets.TextNormal
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.PreviewDarkLightColumn
import eu.codlab.lorcana.blipya.utils.localized
import eu.codlab.lorcana.blipya.widgets.DefaultCard
import eu.codlab.lorcana.blipya.widgets.defaultCardBackground

@Composable
fun DrawSample(
    modifier: Modifier,
    sample: Sample
) {
    DefaultCard(
        modifier,
        elevation = 10.dp,
        backgroundColor = defaultCardBackground(),
        columnModifier = Modifier.drawLeftBorder(2.dp, AppColor.Blue)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextNormal(
                sample.query.localized(),
                fontWeight = Bold
            )

            TextNormal(sample.explanation.localized())
        }
    }
}

fun Modifier.drawLeftBorder(
    width: Dp,
    color: Color,
    shape: Shape = RectangleShape
) = this
    .clip(shape)
    .drawWithContent {
        val widthPx = width.toPx()
        drawContent()
        drawLine(
            color = color,
            start = Offset(widthPx / 2, 0f),
            end = Offset(widthPx / 2, size.height),
            strokeWidth = widthPx
        )
    }

@Preview
@Composable
private fun DrawSamplePreview() {
    PreviewDarkLightColumn(
        Modifier.padding(32.dp),
        submodifier = { Modifier.fillMaxWidth() }
    ) { modifier, _ ->
        DrawSample(
            modifier,
            Sample(
                query = Res.string.documentation_artist_sample_1_query,
                explanation = Res.string.documentation_artist_sample_1_explanation,
            )
        )
    }
}