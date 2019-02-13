package gaston.gsanguinetti.glovo.citypicker.domain.usecase

import com.google.android.gms.maps.model.LatLng
import gaston.gsanguinetti.glovo.base.domain.SingleUseCase
import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.citypicker.domain.model.UserLocation
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCitiesRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.UserLocationRepository
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles

class FetchUserCityCodeUseCase(
    private val userLocationRepository: UserLocationRepository,
    private val availableCitiesRepository: AvailableCitiesRepository,
    private val polygonUtil: PolygonUtil
) : SingleUseCase<String, Unit>() {

    public override fun buildUseCaseObservable(params: Unit?): Single<String> =
        Singles.zip(
            userLocationRepository.fetchUserLocation().setUpForUseCase(),
            availableCitiesRepository.getAvailableCities().setUpForUseCase()
        ) { userLocation, cities -> getCityCodeForLocation(userLocation, cities) }
            .setUpForUseCase()

    private fun getCityCodeForLocation(userLocation: UserLocation, cities: List<AvailableCity>): String {
        cities.forEach {
            val cityPolygons = it.workingArea.map { workingArea -> polygonUtil.decode(workingArea) }
            cityPolygons.forEach { polygon ->
                if (polygonUtil.containsLocation(LatLng(userLocation.latitude, userLocation.longitude), polygon))
                    return it.code
            }
        }
        return String()
    }
}