package eu.codlab.lorcana.blipya.home.navigate

import eu.codlab.lorcana.blipya.model.DeckModel
import eu.codlab.lorcana.math.MulliganScenario
import eu.codlab.lorcana.math.Scenario
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo

sealed class NavigateTo {
    abstract val route: String
    abstract val popBackStack: Boolean
    abstract val options: NavOptions

    object Main : NavigateTo() {
        override val route = "/main"
        override val popBackStack = true

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.First(true)
        )
    }

    object Licenses : NavigateTo() {
        override val route = "/licenses"
        override val popBackStack = true

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.First(true)
        )
    }

    class Deck(deck: DeckModel) : NavigateTo() {
        override val route = "/deck/${deck.id}"
        override val popBackStack = true

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.None
        )
    }

    class DeckMulligan(deck: DeckModel, mulligan: MulliganScenario) : NavigateTo() {
        override val route = "/deck/${deck.id}/mulligan/${mulligan.id}"
        override val popBackStack = false

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.None
        )
    }

    class DeckScenario(deck: DeckModel, scenario: Scenario) : NavigateTo() {
        override val route = "/deck/${deck.id}/scenario/${scenario.id}"
        override val popBackStack = false

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.None
        )
    }

    object CurveInformation : NavigateTo() {
        override val route = "/curve"
        override val popBackStack = true

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.First(true)
        )
    }

    object PlayHubMap : NavigateTo() {
        override val route = "/rph/map"
        override val popBackStack = true

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.First(true)
        )
    }

    object PlayHubOwnRegistrations : NavigateTo() {
        override val route = "/rph/own_registrations"
        override val popBackStack = true

        override val options = NavOptions(
            launchSingleTop = false,
            popUpTo = PopUpTo.First(true)
        )
    }
}
