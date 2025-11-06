package eu.codlab.lorcana.blipya.home.routes

import eu.codlab.navigation.RouteParameterTo
import eu.codlab.navigation.Router

object PossibleRoutes {
    val entries = listOf<Router<*>>(
        RouterMain,
        RouterDeckMulligan,
        RouterDeckScenario,
        RouterDeck,
        RouterLicenses,
        RouterCurve,
        RouterRphMapEvents,
        RouterRphMapStores,
        RouterRphOwnRegistrations,
        RouterCardsListingDocumentation,
        RouterCardsListing
    )

    fun defaultFromUrlLaunched(path: String): RouteParameterTo =
        (entries.firstOrNull { it.isMatching(path) } ?: RouterMain).navigateFrom(path)

    fun fromPath(path: String) = entries.firstOrNull { it.isMatching(path) }
}
