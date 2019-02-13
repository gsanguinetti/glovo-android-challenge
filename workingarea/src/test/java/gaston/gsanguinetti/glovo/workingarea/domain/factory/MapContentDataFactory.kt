package gaston.gsanguinetti.glovo.workingarea.domain.factory

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import gaston.gsanguinetti.glovo.base.test.randomDouble
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.domain.model.CityLatLngBounds
import gaston.gsanguinetti.glovo.workingarea.domain.model.CityPoint
import gaston.gsanguinetti.glovo.workingarea.domain.model.CityPolygons
import gaston.gsanguinetti.glovo.workingarea.domain.model.MapContent

class MapContentDataFactory {
    companion object {
        fun makeMapContent(initialCityCode: String, cityCount :Int = 20, customPolygons: List<List<LatLng>>? = null) =
            MapContent(
                mutableListOf<CityPolygons>()
                    .apply { if(cityCount > 1) repeat(randomInt(1, cityCount)) { add(makeCityPolygons()) } }
                    .apply {
                        add(
                            makeCityPolygons(
                                cityCode = initialCityCode,
                                customPolygons = customPolygons
                            )
                        )
                    }
                    .apply { shuffle() },
                mutableListOf<CityPoint>()
                    .apply { repeat(randomInt(1, 20)) { add(makeCityPoint()) } }
                    .apply {
                        add(
                            makeCityPoint(
                                initialCityCode
                            )
                        )
                    }
                    .apply { shuffle() },
                mutableListOf<CityLatLngBounds>()
                    .apply { repeat(randomInt(1, 20)) { add(makeCityLatLngBounds()) } }
                    .apply {
                        add(
                            makeCityLatLngBounds(
                                initialCityCode
                            )
                        )
                    }
                    .apply { shuffle() }
            )

        fun makeCityPolygons(
            cityCode: String = randomString(), polygonCount: Int = 2, customPolygons: List<List<LatLng>>? = null
        ) =
            CityPolygons(
                cityCode,
                customPolygons ?: mutableListOf<List<LatLng>>().apply {
                    mutableListOf<LatLng>().apply {
                        repeat(randomInt()) {
                            add(LatLng(randomDouble(), randomDouble()))
                        }
                    }
                }
            )

        fun makeCityPoint(cityCode: String = randomString()) =
            CityPoint(cityCode, LatLng(randomDouble(), randomDouble()))

        fun makeCityLatLngBounds(cityCode: String = randomString()): CityLatLngBounds {
            val southwestLat = randomDouble()
            val northestLat = southwestLat + randomDouble()
            val eastestLng = randomDouble()
            val westestLng = eastestLng + randomDouble()
            return CityLatLngBounds(
                cityCode,
                LatLngBounds(LatLng(southwestLat, eastestLng), LatLng(northestLat, westestLng))
            )
        }
    }
}