package eu.codlab.lorcana.blipya.home.routes

enum class PossibleRoutes(val impl: Route) {
    Main(RouteMain()),
    Deck(RouteDeck()),
    DeckMulligan(RouteDeckMulligan()),
    DeckScenario(RouteDeckScenario()),
    ;

    companion object {
        fun fromRoute(route: String) = entries.firstOrNull { it.impl.route == route }
    }
}
