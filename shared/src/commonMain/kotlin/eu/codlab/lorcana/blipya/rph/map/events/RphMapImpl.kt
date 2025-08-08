package eu.codlab.lorcana.blipya.rph.map.events

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RphMapStores(modifier: Modifier) =
    RphMap(modifier) { RphMapStoresModel(it) }

@Composable
fun RphMapEvents(modifier: Modifier) =
    RphMap(modifier) { RphMapEventsModel(it) }
