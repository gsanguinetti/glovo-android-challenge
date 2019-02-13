package gaston.gsanguinetti.glovo.workingarea.presentation.model

import org.locationtech.jts.geom.Coordinate

internal data class NormalizedPolygonCoordinates(
    val externalRing :List<Coordinate>,
    val holes :List<List<Coordinate>>
)