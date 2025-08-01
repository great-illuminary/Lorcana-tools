package eu.codlab.lorcana.blipya.rph.map.events

import androidx.compose.ui.platform.UriHandler
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceZoomable
import eu.codlab.lorcana.blipya.rph.models.Store
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.maps.LatLng
import eu.codlab.maps.MapUtils
import io.ktor.client.call.body
import io.ktor.client.request.get
import ovh.plrapps.mapcompose.api.ExperimentalClusteringApi
import ovh.plrapps.mapcompose.api.addMarker
import ovh.plrapps.mapcompose.api.removeMarker
import ovh.plrapps.mapcompose.ui.state.markers.model.RenderingStrategy

data class RphMapStoresModelState(
    override val currentCoordinates: LatLng? = null,
    override val initialized: Boolean = false,
    val stores: List<Store> = emptyList(),
) : MapModelState

class RphMapStoresModel(
    private val uriHandler: UriHandler
) : MapModel<RphMapStoresModelState, Store>(
    initialState = RphMapStoresModelState(),
    allocateComposer = { mapState, flushCallouts -> ComposeStoreHolder(uriHandler, mapState, flushCallouts) }
), MapInterfaceZoomable {
    @OptIn(ExperimentalClusteringApi::class)
    private fun startLoadingData() = safeLaunch(
        onError = {
            it.printStackTrace()
        }
    ) {
        val url = "https://api-lorcana.com/rph/stores"
        println("url -> $url")
        val result = try {
            client.get(url)
        } catch (err: Throwable) {
            err.printStackTrace()
            null
        }

        val stores = result?.body<List<Store>>()
            ?.filter { null != it.latLng() } ?: emptyList()

        println("having ${stores.size} results")

        states.value.stores.forEach { (store) -> mapState.removeMarker("store_${store}") }

        updateState {
            copy(
                stores = stores
            )
        }

        stores.forEach { store ->
            val latLng = store.latLng()!!
            val xy = MapUtils.latLngToXY(latLng.latitude, latLng.longitude)

            mapState.addMarker(
                "store_${store.id}",
                xy.x,
                xy.y,
                renderingStrategy = RenderingStrategy.Clustering("default"),
                c = composer.compose(store, xy)
            )
        }
    }

    override suspend fun startLoadingData(state: RphMapStoresModelState) {
        startLoadingData()
    }

    override suspend fun setInitialized(initialized: Boolean) =
        updateState { copy(initialized = true) }

    override suspend fun setCurrentCoordinates(newCoordinates: LatLng) =
        updateState { copy(currentCoordinates = newCoordinates) }

}
