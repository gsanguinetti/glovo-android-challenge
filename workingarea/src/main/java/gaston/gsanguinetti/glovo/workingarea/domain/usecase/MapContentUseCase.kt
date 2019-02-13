package gaston.gsanguinetti.glovo.workingarea.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import gaston.gsanguinetti.glovo.base.domain.SingleUseCase
import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import gaston.gsanguinetti.glovo.workingarea.domain.model.*
import gaston.gsanguinetti.glovo.workingarea.domain.repository.WorkingAreaCitiesRepository
import io.reactivex.Single

class MapContentUseCase(
    private val availableCitiesRepository: WorkingAreaCitiesRepository,
    private val polygonUtil: PolygonUtil
) : SingleUseCase<MapContent, Unit>() {

    public override fun buildUseCaseObservable(params: Unit?): Single<MapContent> =
        availableCitiesRepository.getAvailableCities()
            .setUpForUseCase()
            .map { cityList -> cityList.map { buildCityArea(it) } }
            .map { cityArea ->
                val cityLatLngBounds = cityArea.map { CityLatLngBounds(it.first, makeLatLngBoundsFor(it.second)) }
                MapContent(
                    cityArea.map { CityPolygons(it.first, it.second) },
                    cityLatLngBounds.map { CityPoint(it.cityCode, it.latLngBounds.center) },
                    cityLatLngBounds
                )
            }

    private fun buildCityArea(availableCity: AvailableCity): Pair<String, List<List<LatLng>>> {
        return Pair(availableCity.code, availableCity.workingArea.map { polygonUtil.decode(it) })
    }

    private fun makeLatLngBoundsFor(locations: List<List<LatLng>>) =
        LatLngBounds.Builder().apply {
            locations.forEach { locationList -> locationList.forEach { include(it) } }
        }.build()
}