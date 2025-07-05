package eu.codlab.maps.providers

class GoogleProvider : AbstractMapProvider("google", true) {
    override suspend fun buildUrl(row: Int, col: Int, zoomLvl: Int): String {
        val path = "${zoomLvl + 1}/$col/$row"
        return "https://api-lorcana.com/rph/map/tile/$path"
    }
}