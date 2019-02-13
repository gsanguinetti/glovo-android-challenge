package gaston.gsanguinetti.glovo.base.domain.util

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil

class PolygonUtil {
    fun decode (encodedPath :String) :List<LatLng> = PolyUtil.decode(encodedPath)
    fun containsLocation(latLng: LatLng, polygonCoordinates : List<LatLng>) :Boolean =
            PolyUtil.containsLocation(latLng, polygonCoordinates, false)
}