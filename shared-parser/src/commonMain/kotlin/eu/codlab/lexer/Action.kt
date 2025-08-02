package eu.codlab.lexer

enum class Action {
    Cost,
    Attack,
    Move,
    Defence,
    Artist,
    Set,
    Id,
    Rarity;

    companion object {
        private val cache = Action.entries.associateBy { it.name.lowercase() }

        fun get(action: String) = cache[action]

        fun String.toAction() = get(this)
    }
}