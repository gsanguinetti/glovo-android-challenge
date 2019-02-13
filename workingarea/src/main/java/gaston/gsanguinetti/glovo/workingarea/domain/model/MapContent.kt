package gaston.gsanguinetti.glovo.workingarea.domain.model

data class MapContent(
    val workingAreaPolygons: List<CityPolygons>,
    val cityMapPoints: List<CityPoint>,
    val cityLatLngBounds: List<CityLatLngBounds>
)