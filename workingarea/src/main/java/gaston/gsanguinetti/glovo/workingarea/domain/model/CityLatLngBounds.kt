package gaston.gsanguinetti.glovo.workingarea.domain.model

import com.google.android.gms.maps.model.LatLngBounds

data class CityLatLngBounds(
    val cityCode :String,
    val latLngBounds: LatLngBounds
)