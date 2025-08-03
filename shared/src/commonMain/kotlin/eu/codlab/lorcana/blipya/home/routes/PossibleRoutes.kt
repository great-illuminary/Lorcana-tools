package eu.codlab.lorcana.blipya.home.routes

object PossibleRoutes {
    val Main = RouteMain()

    // first add the mulligan & scenario for the possible navigation's fallback to /deck
    val DeckMulligan = RouteDeckMulligan()
    val DeckScenario = RouteDeckScenario()
    val Deck = RouteDeck()
    val RouteLicenses = RouteLicenses()
    val RouteCurve = RouteCurve()
    val RouteRphMapEvents = RouteRphMapEvents()
    val RouteRphMapStores = RouteRphMapStores()
    val RouteRphOwnRegistrations = RouteRphOwnRegistrations()
    val RouteCardsListing = RouteCardsListing()

    val entries = listOf(
        Main,
        DeckMulligan,
        DeckScenario,
        Deck,
        RouteLicenses,
        RouteCurve,
        RouteRphMapEvents,
        RouteRphMapStores,
        RouteRphOwnRegistrations,
        RouteCardsListing
    )

    fun fromRoute(route: String) = entries.firstOrNull { it.route == route }

    fun fromPath(path: String) = entries.firstOrNull { it.isMatching(path) }
}
