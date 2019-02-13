package gaston.gsanguinetti.glovo.workingarea.presentation.mapper

import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.domain.factory.MapContentDataFactory
import gaston.gsanguinetti.glovo.workingarea.presentation.model.TestPolygon
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

@RunWith(JUnit4::class)
class MapPresentationContentMapperTest {

    private lateinit var mapPresentationContentMapper: MapPresentationContentMapper

    @Before
    fun before() {
        mapPresentationContentMapper = MapPresentationContentMapper(GeometryNormalizer())
    }

    @Test
    fun mapFromMapContent() {
        val mapContent = MapContentDataFactory.makeMapContent(randomString(), 1, makeCustomOverlappedPolygons())
        mapPresentationContentMapper.mapToEntity(mapContent).run {
            mapContent.cityLatLngBounds.forEachIndexed { index, (cityCode, latLngBounds) ->
                assertEquals(cityCode, cityLatLngBounds[index].first)
                assertEquals(latLngBounds, cityLatLngBounds[index].second)
            }
            mapContent.cityMapPoints.forEachIndexed { index, (cityCode, position) ->
                assertEquals(cityCode, cityMarkers[index].title)
                assertEquals(position, cityMarkers[index].position)
            }

            // Should generate one combined polygon
            assert(citiesPolygons.size == 1)

            val combinedPolygon = ArrayList(parsePolygon("combined_polygon.json"))

            //Compare the polygons
            val generatedPolygon = citiesPolygons.first().points
            assertEquals(combinedPolygon.size, generatedPolygon.size)
            combinedPolygon.forEach { coordinate -> assert(generatedPolygon.find { it == coordinate } != null) }
        }
    }

    /**
     * This method reads 2 overlapped polygons from the tests resources folder
     * in order to test the combination of them.
     *
     * The values and properties of the coordinates can be checked at:
     * - 1st polygon: https://goo.gl/Bzvnvp
     * - 2nd polygon: https://goo.gl/upimM4
     * - Combined polygon: https://goo.gl/Hj5LaZ
     */
    private fun makeCustomOverlappedPolygons() =
        mutableListOf(
            parsePolygon("polygon1.json"),
            parsePolygon("polygon2.json")
        )

    private fun parsePolygon(fileName: String): List<LatLng> {
        val classLoader = javaClass.classLoader!!
        val testPolygon =
            Gson().fromJson(File(classLoader.getResource(fileName).path).readText(), TestPolygon::class.java)
        return testPolygon.coordinates[0].map { LatLng(it[0], it[1]) }
    }
}