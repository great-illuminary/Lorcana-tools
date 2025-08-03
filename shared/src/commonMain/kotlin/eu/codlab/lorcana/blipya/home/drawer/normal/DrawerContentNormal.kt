package eu.codlab.lorcana.blipya.home.drawer.normal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.drick.compose.hotpreview.HotPreview
import eu.codlab.blipya.config.SharedConfig
import eu.codlab.blipya.res.Res
import eu.codlab.blipya.res.curve_title
import eu.codlab.blipya.res.decks_title
import eu.codlab.blipya.res.licenses_title
import eu.codlab.blipya.res.menu_cards_title
import eu.codlab.blipya.res.menu_playhub_title
import eu.codlab.blipya.res.menu_title_main
import eu.codlab.blipya.res.menu_title_others
import eu.codlab.blipya.res.rph_map_stores_title
import eu.codlab.blipya.res.rph_map_title
import eu.codlab.blipya.res.rph_own_registrations
import eu.codlab.blipya.res.title_cards_listing
import eu.codlab.compose.theme.LocalDarkTheme
import eu.codlab.compose.widgets.spacers.BottomSpacer
import eu.codlab.compose.widgets.spacers.TopSpacer
import eu.codlab.lorcana.blipya.home.HotPreviewApp
import eu.codlab.lorcana.blipya.home.drawer.DrawerItem
import eu.codlab.lorcana.blipya.home.drawer.DrawerSeparator
import eu.codlab.lorcana.blipya.home.drawer.DrawerTitle
import eu.codlab.lorcana.blipya.home.drawer.systemBackground
import eu.codlab.lorcana.blipya.home.routes.PossibleRoutes
import eu.codlab.lorcana.blipya.home.routes.Route
import eu.codlab.lorcana.blipya.icons.GridView
import eu.codlab.lorcana.blipya.icons.MapLocation
import eu.codlab.lorcana.blipya.icons.PieChart
import eu.codlab.lorcana.blipya.theme.AppColor
import eu.codlab.lorcana.blipya.utils.localized

@Suppress("LongMethod", "UnusedPrivateMember")
@Composable
fun DrawerContentNormal(
    modifier: Modifier = Modifier,
    currentRoute: Route? = null,
    tiny: Boolean = false,
    onMenuItemSelected: (title: String, navigateTo: Route) -> Unit
) {
    val dark = LocalDarkTheme.current

    println("showing tiny ? $tiny")
    Column(
        modifier = modifier
            .systemBackground()
            .verticalScroll(rememberScrollState())
            .background(
                color = if (dark) {
                    AppColor.BackgroundBlue
                } else {
                    AppColor.White
                }
            )
    ) {
        TopSpacer()

        if (!tiny) {
            DrawerTitle(text = Res.string.menu_title_main.localized())
        } else {
            // nothing
        }

        DrawerItem(
            text = Res.string.decks_title.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.Main,
            image = Icons.Rounded.GridView,
            onClick = onMenuItemSelected,
            tiny = tiny
        )

        DrawerItem(
            text = Res.string.curve_title.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.RouteCurve,
            image = Icons.Rounded.PieChart,
            onClick = onMenuItemSelected,
            tiny = tiny
        )

        DrawerSeparator()

        if (!tiny) {
            DrawerTitle(text = Res.string.menu_cards_title.localized())
        } else {
            // nothing
        }

        DrawerItem(
            text = Res.string.title_cards_listing.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.RouteCardsListing,
            image = Icons.Rounded.GridView,
            onClick = onMenuItemSelected,
            tiny = tiny
        )

        DrawerSeparator()

        if (!tiny) {
            DrawerTitle(text = Res.string.menu_playhub_title.localized())
        } else {
            // nothing
        }

        DrawerItem(
            text = Res.string.rph_map_title.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.RouteRphMapEvents,
            image = Icons.Rounded.MapLocation,
            onClick = onMenuItemSelected,
            tiny = tiny
        )

        DrawerItem(
            text = Res.string.rph_map_stores_title.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.RouteRphMapStores,
            image = Icons.Rounded.MapLocation,
            onClick = onMenuItemSelected,
            tiny = tiny
        )

        DrawerItem(
            text = Res.string.rph_own_registrations.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.RouteRphOwnRegistrations,
            image = Icons.Rounded.AccountBox,
            onClick = onMenuItemSelected,
            tiny = tiny
        )


        Column(Modifier.weight(1.0f)) { }

        DrawerSeparator()

        if (!tiny) {
            DrawerTitle(text = Res.string.menu_title_others.localized())
        } else {
            // nothing
        }

        DrawerItem(
            text = Res.string.licenses_title.localized(),
            currentRoute = currentRoute,
            route = PossibleRoutes.RouteLicenses,
            image = Icons.Rounded.Info,
            onClick = onMenuItemSelected,
            tiny = tiny
        )

        Spacer(Modifier.height(8.dp))

        if (!tiny) {
            DrawerTitle(
                text = "v${SharedConfig.version}"
            )

            Spacer(Modifier.height(8.dp))
        }

        BottomSpacer()
    }
}

@HotPreview(widthDp = 300, heightDp = 900, darkMode = true)
@HotPreview(widthDp = 300, heightDp = 900, darkMode = false)
@Composable
private fun PreviewDrawerContent() {
    HotPreviewApp {
        DrawerContentNormal(
            tiny = false
        ) { _, _ ->
            // nothing}
        }
    }
}
