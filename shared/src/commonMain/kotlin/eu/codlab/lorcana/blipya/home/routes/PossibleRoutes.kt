package eu.codlab.lorcana.blipya.home.routes

enum class PossibleRoutes(val impl: Route) {
    Main(RouteMain()),
    Deck(RouteDeck()),
    DeckMulligan(RouteDeckMulligan()),
    DeckScenario(RouteDeckScenario()),
    RouteLicenses(RouteLicenses()),
    RouteCurve(RouteCurve()),
    RouteRphMap(RouteRphMap())
    ;

    companion object {
        fun fromRoute(route: String) = entries.firstOrNull { it.impl.route == route }
    }
}
