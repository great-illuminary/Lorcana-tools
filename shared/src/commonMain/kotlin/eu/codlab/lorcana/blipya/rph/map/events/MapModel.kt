package eu.codlab.lorcana.blipya.rph.map.events

import eu.codlab.http.Configuration
import eu.codlab.http.createClient
import eu.codlab.lorcana.blipya.rph.map.map.interfaces.MapInterfaceZoomable
import eu.codlab.lorcana.blipya.utils.safeLaunch
import eu.codlab.maps.LatLng
import eu.codlab.maps.MapUtils
import eu.codlab.maps.providers.GoogleProvider
import eu.codlab.viewmodel.StateViewModel
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
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

    private val levelCount = 18
    private val fullWidthOrHeight = 256 * 2.0.pow(levelCount).toInt()

    val mapState = MapState(
        levelCount,
        fullWidthOrHeight,
        fullWidthOrHeight,
        tileSize = 256
    ).apply {
        setMagnifyingFactor(1)

        maxScale = MAX_ZOOM
        scale = DEFAULT_ZOOM
        addLayer(tileStreamProvider)

        enableRotation()
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

            setZoom(MIN_ZOOM)
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
        mapState.scale = DEFAULT_ZOOM

        safeLaunch {
            val latLng = LatLng(0.0, 0.0)
            val xy = MapUtils.latLngToXY(latLng)

            mapState.snapScrollTo(
                xy.x,
                xy.y
            )
        }
    }

    override fun zoomIn() = applyZoom(1.1)

    override fun zoomOut() = applyZoom(0.9)

    private fun applyZoom(coef: Double) {
        // make sure that we are both 3.0f > scale > 0.05f
        val newZoom = mapState.scale * coef
        setZoom(max(min(newZoom, MAX_ZOOM), MIN_ZOOM))
    }

    private fun setZoom(zoom: Double) {
        mapState.scale = zoom

        safeLaunch {
            states.value.currentCoordinates!!.let {
                println("having coordinates ? $it")
                val coords = MapUtils.latLngToXY(it.latitude, it.longitude)
                println("snapTo $coords")
                mapState.snapScrollTo(coords.x, coords.y)
            }
        }
    }
}

interface MapModelState {
    val initialized: Boolean
    val currentCoordinates: LatLng?
}
