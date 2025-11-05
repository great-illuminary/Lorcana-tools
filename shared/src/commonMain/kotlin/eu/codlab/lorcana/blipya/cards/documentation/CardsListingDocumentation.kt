package eu.codlab.lorcana.blipya.cards.documentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.blipya.res.*
import eu.codlab.lorcana.blipya.home.AppModel

@Composable
fun CardsListingDocumentation(
    modifier: Modifier = Modifier,
    appModel: AppModel
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1)
    ) {
        items(documentation.size) { id ->
            DrawSection(
                Modifier.fillMaxWidth(),
                documentation[id]
            )
        }
    }
}

val documentation = listOf(
    Section(
        Res.string.documentation_text_title,
        Res.string.documentation_text_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_cost_inkwell_title,
        Res.string.documentation_cost_inkwell_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_strength_willpower_title,
        Res.string.documentation_strength_willpower_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_lore_title,
        Res.string.documentation_lore_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_name_version_classification_title,
        Res.string.documentation_name_version_classification_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_move_title,
        Res.string.documentation_move_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_set_id_rarity_title,
        Res.string.documentation_set_id_rarity_text,
        emptyList()
    ),
    Section(
        Res.string.documentation_artist_title,
        Res.string.documentation_artist_text,
        listOf(
            Sample(
                Res.string.documentation_artist_sample_1_query,
                Res.string.documentation_artist_sample_1_explanation
            )
        )
    )
)