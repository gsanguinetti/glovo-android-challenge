package gaston.gsanguinetti.glovo.citypicker.domain.factory

import com.google.android.gms.maps.model.LatLng
import gaston.gsanguinetti.glovo.base.test.randomDouble

class PolygonCoordinateListFactory {
    companion object {
        fun makePolyconCoordinateList(numCoordinates :Int) =
            mutableListOf<LatLng>().apply {
                repeat(numCoordinates) { add(LatLng(randomDouble(), randomDouble())) }
            }
    }
}