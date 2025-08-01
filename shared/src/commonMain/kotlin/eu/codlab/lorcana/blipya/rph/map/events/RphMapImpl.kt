package eu.codlab.lorcana.blipya.rph.map.events

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.codlab.lorcana.blipya.home.AppModel

@Composable
fun RphMapStores(
    modifier: Modifier,
    appModel: AppModel
) = RphMap(
    modifier,
    appModel
) { RphMapStoresModel(it) }

@Composable
fun RphMapEvents(
    modifier: Modifier,
    appModel: AppModel
) = RphMap(
    modifier,
    appModel
) { RphMapEventsModel(it) }
