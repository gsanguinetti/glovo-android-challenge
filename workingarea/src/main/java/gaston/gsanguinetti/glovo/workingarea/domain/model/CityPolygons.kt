package gaston.gsanguinetti.glovo.workingarea.domain.model

import com.google.android.gms.maps.model.LatLng

data class CityPolygons (
    val cityCode :String,
    val polygonsCoordinates :List<List<LatLng>>
)