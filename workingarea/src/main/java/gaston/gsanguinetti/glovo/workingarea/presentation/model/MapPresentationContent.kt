package gaston.gsanguinetti.glovo.workingarea.presentation.model

import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

data class MapPresentationContent (
    val cityMarkers :List<MarkerOptions>,
    val citiesPolygons :List<PolygonOptions>,
    val cityLatLngBounds :List<Pair<String, LatLngBounds>>
)