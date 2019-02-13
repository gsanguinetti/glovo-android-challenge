package gaston.gsanguinetti.glovo.workingarea.domain.usecase

import com.google.android.gms.maps.model.LatLng
import gaston.gsanguinetti.glovo.base.domain.MaybeUseCase
import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.repository.CityDetailsRepository
import gaston.gsanguinetti.glovo.workingarea.domain.repository.WorkingAreaCitiesRepository
import io.reactivex.Maybe

class CityDetailsUseCase(
    private val workingAreaCitiesRepository: WorkingAreaCitiesRepository,
    private val cityDetailsRepository: CityDetailsRepository,
    private val polygonUtil: PolygonUtil
) : MaybeUseCase<AvailableCityDetails, LatLng>() {

    public override fun buildUseCaseObservable(params: LatLng?): Maybe<AvailableCityDetails> =
        if (params == null) Maybe.empty()
        else {
            workingAreaCitiesRepository.getAvailableCities()
                .map { cityList ->
                    cityList.map { (code, workingArea) ->
                        Pair(
                            code,
                            workingArea.map { polygonUtil.decode(it) })
                    }
                }
                .toMaybe()
                .setUpForUseCase()
                .flatMap { cityPolygonsList ->
                    val city = cityPolygonsList.find { cityPolygons ->
                        cityPolygons.second.find { polygonUtil.containsLocation(params, it) } != null
                    }
                    if (city == null) Maybe.empty<AvailableCityDetails>()
                    else cityDetailsRepository.getCityDetails(city.first).toMaybe().setUpForUseCase()
                }
        }
}