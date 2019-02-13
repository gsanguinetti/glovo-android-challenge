package gaston.gsanguinetti.glovo.workingarea.presentation.mapper

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.workingarea.domain.model.CityPolygons
import gaston.gsanguinetti.glovo.workingarea.domain.model.MapContent
import gaston.gsanguinetti.glovo.workingarea.presentation.model.MapPresentationContent
import gaston.gsanguinetti.glovo.workingarea.presentation.model.NormalizedPolygonCoordinates
import org.locationtech.jts.geom.*
import org.locationtech.jts.operation.polygonize.Polygonizer
import java.util.*


class MapPresentationContentMapper(private val geometryNormalizer: GeometryNormalizer) :
    DataMapper<MapPresentationContent, MapContent> {

    override fun mapFromEntity(entity: MapPresentationContent): MapContent {
        throw NotImplementedError()
    }

    override fun mapToEntity(domainModel: MapContent): MapPresentationContent =
        MapPresentationContent(
            domainModel.cityMapPoints.map { MarkerOptions().position(it.position).title(it.cityCode) },

            domainModel.workingAreaPolygons
                .buildCitiesNormalizedPolygons()
                .detectAndCombinePolygonsForOverlappedCities()
                .buildPolygonOptions(),

            domainModel.cityLatLngBounds.map { Pair(it.cityCode, it.latLngBounds) }
        )

    private fun List<CityPolygons>.buildCitiesNormalizedPolygons(): List<NormalizedPolygonCoordinates> =
        map { cityPolygons ->
            cityPolygons.polygonsCoordinates.filter { isValidPolygonCoordinates(it) }
                .createPolygons()
                .mapNotNull { geometryNormalizer.normalize(it) }
                .combineGeometriesAndCreateCityPolygon()
        }

    private fun List<List<LatLng>>.createPolygons(): List<Polygon> =
        ArrayList<Polygon>(
            this.map { polygon ->
                val polygonRing = ArrayList(polygon.map { Coordinate(it.latitude, it.longitude) })

                //Close the ring coordinates adding the first coordinate again
                if (polygonRing.isNotEmpty()) polygonRing.add(polygonRing.first())

                GeometryFactory().createPolygon(polygonRing.toTypedArray())
            })

    private fun List<Geometry>.combineGeometriesAndCreateCityPolygon(): NormalizedPolygonCoordinates =
        GeometryCollection(toTypedArray(), GeometryFactory()).union().toNormalizedPolygonCoordinates()

    private fun List<NormalizedPolygonCoordinates>.detectAndCombinePolygonsForOverlappedCities()
            : List<NormalizedPolygonCoordinates> {
        var areCitiesOverlapped = true
        val cityGeometryPolygons = ArrayList(map { it.toPolygon() })

        while (areCitiesOverlapped) {
            areCitiesOverlapped = false
            val iterator = cityGeometryPolygons.listIterator()
            while (iterator.hasNext()) {
                val cityPolygon = iterator.next()
                val overlappedCity = cityGeometryPolygons.find { it != cityPolygon && it.overlaps(cityPolygon) }
                if (overlappedCity != null) {
                    areCitiesOverlapped = true
                    cityGeometryPolygons.add(
                        listOf(cityPolygon, overlappedCity).combineGeometriesAndCreateCityPolygon().toPolygon()
                    )
                    cityGeometryPolygons.remove(cityPolygon)
                    cityGeometryPolygons.remove(overlappedCity)
                    break
                }
            }
        }
        return cityGeometryPolygons.map { it.toNormalizedPolygonCoordinates() }
    }

    private fun NormalizedPolygonCoordinates.toPolygon(): Polygon =
        GeometryFactory().run {
            Polygon(
                createLinearRing(externalRing.toTypedArray()),
                holes.map { createLinearRing(it.toTypedArray()) }.toTypedArray(),
                this
            )
        }

    private fun Geometry.toNormalizedPolygonCoordinates(): NormalizedPolygonCoordinates {
        val polygonMaker = Polygonizer()
        polygonMaker.add(this)

        val polygonHoles = mutableListOf<List<Coordinate>>()
        polygonMaker.polygons.map { it as Polygon }.forEach { polygon ->
            polygonHoles.addAll((0 until polygon.numInteriorRing).map {
                polygon.getInteriorRingN(it).coordinates.toList()
            })
        }

        return NormalizedPolygonCoordinates(
            (polygonMaker.polygons.first() as Polygon).exteriorRing.coordinates.toList(),
            polygonHoles
        )
    }

    private fun List<NormalizedPolygonCoordinates>.buildPolygonOptions(): List<PolygonOptions> =
        map { (externalRing, holes) ->
            val polygonOptions = PolygonOptions()
            polygonOptions.addAll(externalRing.map { LatLng(it.x, it.y) })
            holes.forEach { hole -> polygonOptions.addHole(hole.map { LatLng(it.x, it.y) }) }
            polygonOptions
        }

    private fun isValidPolygonCoordinates(polygonCoordinates: List<LatLng>): Boolean = polygonCoordinates.size > 3

}