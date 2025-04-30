package eu.codlab.lorcana.blipya.deck.card

data class DeckMap(
    val characters: List<VirtualCardNumber>,
    val actions: List<VirtualCardNumber>,
    val songs: List<VirtualCardNumber>,
    val objects: List<VirtualCardNumber>,
    val locations: List<VirtualCardNumber>,
)
