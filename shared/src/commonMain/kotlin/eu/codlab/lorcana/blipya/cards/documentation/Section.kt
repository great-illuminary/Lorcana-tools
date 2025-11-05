package eu.codlab.lorcana.blipya.cards.documentation

import org.jetbrains.compose.resources.StringResource

data class Section(
    val title: StringResource,
    val block: StringResource,
    val samples: List<Sample>
)