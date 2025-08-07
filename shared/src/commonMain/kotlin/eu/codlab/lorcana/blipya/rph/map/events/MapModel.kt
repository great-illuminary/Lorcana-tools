package eu.codlab.lorcana.blipya.rph.map.events

import eu.codlab.http.Configuration
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceZoomable
import eu.codlab.lorcana.blipya.utils.MapManipulation
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.maps.LatLng
import eu.codlab.maps.MapUtils
import eu.codlab.maps.providers.GoogleProvider
import eu.codlab.viewmodel.StateViewModel
import ovh.plrapps.mapcompose.api.ExperimentalClusteringApi
import ovh.plrapps.mapcompose.api.addClusterer
import ovh.plrapps.mapcompose.api.addLayer
import ovh.plrapps.mapcompose.api.centroidSnapshotFlow
import ovh.plrapps.mapcompose.api.enableRotation
import ovh.plrapps.mapcompose.api.maxScale
import ovh.plrapps.mapcompose.api.scale
import ovh.plrapps.mapcompose.api.setMagnifyingFactor
import ovh.plrapps.mapcompose.api.snapScrollTo
import ovh.plrapps.mapcompose.core.TileStreamProvider
import ovh.plrapps.mapcompose.ui.state.MapState

abstract class MapModel<T : MapModelState, H>(
    initialState: T,
    allocateComposer: (
        mapState: MapState,
        flushCallouts: () -> Unit
    ) -> Composer<H>
) : StateViewModel<T>(initialState),
    MapInterfaceZoomable {
    protected val client = createClient(
        Configuration(
            enableLogs = true
        )
    ) { }
    private val googleApi = GoogleProvider()

    private val tileStreamProvider = TileStreamProvider { row, col, zoomLvl ->
        googleApi.getTile(row, col, zoomLvl)
    }

    private val mapManipulation = MapManipulation(
        tileSize = 256,
        levelCount = 18
    )

    @OptIn(ExperimentalClusteringApi::class)
    val mapState = MapState(
        mapManipulation.levelCount,
        mapManipulation.fullWidthOrHeight,
        mapManipulation.fullWidthOrHeight,
        tileSize = mapManipulation.tileSize
    ).apply {
        setMagnifyingFactor(1)

        maxScale = mapManipulation.maxZoom
        scale = mapManipulation.defaultZoom
        addLayer(tileStreamProvider)

        enableRotation()

        addClusterer("default") { ids ->
            { composer.cluster(size = ids.size) }
        }
    }

    private fun flushCallouts() {
        mapState.onPress()
    }

    protected val composer: Composer<H> = createCompose(allocateComposer)

    fun createCompose(
        allocateComposer: (
            mapState: MapState,
            flushCallouts: () -> Unit
        ) -> Composer<H>
    ) = allocateComposer(
        mapState,
    ) { flushCallouts() }

    init {
        val latLng = LatLng(0.0, 0.0)
        val xy = MapUtils.latLngToXY(latLng)

        safeLaunch(
            onError = {
                it.printStackTrace()
            }
        ) {
            googleApi.initialize()

            startLoadingData(states.value)

            setInitialized(true)

            setZoom(mapManipulation.minZoom)
            mapState.snapScrollTo(
                xy.x,
                xy.y
            )

            mapState.centroidSnapshotFlow().collect {
                val coords = MapUtils.xyToLatLng(it.x, it.y)
                setCurrentCoordinates(coords)
            }
        }
    }

    protected abstract suspend fun startLoadingData(state: T)

    protected abstract suspend fun setInitialized(initialized: Boolean)

    protected abstract suspend fun setCurrentCoordinates(newCoordinates: LatLng)

    override fun moveToOrigin() {
        mapState.scale = mapManipulation.defaultZoom

        safeLaunch {
            val latLng = LatLng(0.0, 0.0)
            val xy = MapUtils.latLngToXY(latLng)

            mapState.snapScrollTo(
                xy.x,
                xy.y
            )
        }
    }

    override fun zoomIn() = applyZoom(mapManipulation.zoomInCoeff)

    override fun zoomOut() = applyZoom(mapManipulation.zoomOutCoeff)

    private fun applyZoom(coef: Double) {
        // make sure that we are both 3.0f > scale > 0.05f
        val newZoom = mapState.scale * coef
        setZoom(mapManipulation.actualZoom(newZoom))
    }

    private fun setZoom(zoom: Double) {
        mapState.scale = zoom

        safeLaunch {
            states.value.currentCoordinates!!.let {
                val coords = MapUtils.latLngToXY(it.latitude, it.longitude)
                mapState.snapScrollTo(coords.x, coords.y)
            }
        }
    }
}

interface MapModelState {
    val initialized: Boolean
    val currentCoordinates: LatLng?
}
