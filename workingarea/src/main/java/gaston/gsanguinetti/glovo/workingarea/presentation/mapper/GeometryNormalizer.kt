package gaston.gsanguinetti.glovo.workingarea.presentation.mapper

import org.locationtech.jts.geom.*
import org.locationtech.jts.operation.polygonize.Polygonizer


class GeometryNormalizer {

    fun normalize(geometry: Geometry): Geometry? {
        when (geometry) {
            is Polygon -> {
                if (geometry.isValid()) {
                    geometry.normalize()
                    return geometry
                }
                val polygonizer = Polygonizer()
                addPolygon(geometry, polygonizer)
                return toPolygonGeometry(polygonizer.polygons.map { it as Polygon })
            }
            is MultiPolygon -> {
                if (geometry.isValid()) {
                    geometry.normalize()
                    return geometry
                }
                val polygonizer = Polygonizer()
                (0 until geometry.numGeometries).forEach {
                    addPolygon(
                        geometry.getGeometryN(it) as Polygon,
                        polygonizer
                    )
                }
                return toPolygonGeometry(polygonizer.polygons.map { it as Polygon })
            }
            else -> return geometry
        }
    }

    private fun addPolygon(polygon: Polygon, polygonizer: Polygonizer) {
        addLineString(polygon.exteriorRing, polygonizer)
        (0 until polygon.numInteriorRing).forEach { addLineString(polygon.getInteriorRingN(it), polygonizer) }
    }

    private fun addLineString(lineString: LineString, polygonizer: Polygonizer) {
        var newLineString = lineString

        if (newLineString is LinearRing)
            newLineString = lineString.factory.createLineString(lineString.coordinateSequence)

        val point = newLineString.factory.createPoint(lineString.getCoordinateN(0))
        polygonizer.add(newLineString.union(point))
    }

    private fun toPolygonGeometry(polygons: Collection<Polygon>): Geometry? =
        when (polygons.size) {
            0 -> null // No valid polygons
            1 -> polygons.iterator().next() // single polygon - no need to wrap
            else -> {
                //polygons may still overlap! Need to sym difference them
                val iterator = polygons.iterator()
                var geometry: Geometry = iterator.next()
                while (iterator.hasNext()) {
                    geometry = geometry.symDifference(iterator.next())
                }
                geometry
            }
        }
}