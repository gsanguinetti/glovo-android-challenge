package gaston.gsanguinetti.glovo.workingarea.presentation.factory

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import gaston.gsanguinetti.glovo.base.test.randomBoolean
import gaston.gsanguinetti.glovo.base.test.randomDouble
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.presentation.model.CityDetailsContent
import gaston.gsanguinetti.glovo.workingarea.presentation.model.MapPresentationContent

class MapPresentationContentDataFactory {
    companion object {

        fun makeMapPresentationContent(initialCityCode: String) =
            MapPresentationContent(
                mutableListOf<MarkerOptions>()
                    .apply { repeat(randomInt(1, 20)) { add(makeCityMarker()) } }
                    .apply { add(makeCityMarker(initialCityCode)) }
                    .apply { shuffle() },
                mutableListOf<PolygonOptions>()
                    .apply { repeat(randomInt(1, 20)) { add(makePolygonOptions()) } },
                mutableListOf<Pair<String, LatLngBounds>>()
                    .apply { repeat(randomInt(1, 20)) { add(makeCityLatLngBound()) } }
                    .apply { add(makeCityLatLngBound(initialCityCode)) }
                    .apply { shuffle() }
            )

        fun makeCityMarker(cityCode: String = randomString()) =
            MarkerOptions().title(cityCode).position(LatLng(randomDouble(), randomDouble()))!!

        fun makePolygonOptions() =
            PolygonOptions().apply { repeat(randomInt()) { add(LatLng(randomDouble(), randomDouble())) } }

        fun makeCityLatLngBound(cityCode: String = randomString()): Pair<String, LatLngBounds> {
            val southwestLat = randomDouble()
            val northestLat = southwestLat + randomDouble()
            val eastestLng = randomDouble()
            val westestLng = eastestLng + randomDouble()
            return Pair(cityCode, LatLngBounds(LatLng(southwestLat, eastestLng), LatLng(northestLat, westestLng)))
        }

        fun makeCityDetailsContent(cityCode: String = randomString()) =
                CityDetailsContent(
                    randomString(),
                    randomBoolean(),
                    randomString(),
                    randomString(),
                    randomString()
                )
    }
}