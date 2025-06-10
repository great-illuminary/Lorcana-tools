package eu.codlab.lorcana.blipya.deck.card

data class DeckMap(
    val characters: List<VirtualCardNumber>,
    val actions: List<VirtualCardNumber>,
    val songs: List<VirtualCardNumber>,
    val objects: List<VirtualCardNumber>,
    val locations: List<VirtualCardNumber>,
) {
    val all = characters + actions + songs + objects + locations

    fun uninkables() = all.filter { !it.card.inkwell }.map { it.number }.sum()

    fun curved(): List<Long> {
        val map = mutableMapOf<Int, Long>()

        var max = 0
        all.forEach { if (it.card.cost > max) max = it.card.cost }

        (0..max).forEach { map[it] = 0 }

        all.forEach { map[it.card.cost] = map[it.card.cost]!! + it.number }

        return map.keys.sorted().map { map[it] ?: 0 }
    }


    fun curvedInkables(): List<Long> {
        val map = mutableMapOf<Int, Long>()

        var max = 0
        all.forEach { if (it.card.cost > max) max = it.card.cost }

        (0..max).forEach { map[it] = 0 }

        all.forEach {
            if (!it.card.inkwell) return@forEach

            map[it.card.cost] = map[it.card.cost]!! + it.number
        }

        return map.keys.sorted().map { map[it] ?: 0 }
    }
}
