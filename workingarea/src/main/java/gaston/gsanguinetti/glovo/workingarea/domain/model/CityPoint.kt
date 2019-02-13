package gaston.gsanguinetti.glovo.workingarea.domain.model

import com.google.android.gms.maps.model.LatLng

data class CityPoint(
    val cityCode: String,
    val position: LatLng
)