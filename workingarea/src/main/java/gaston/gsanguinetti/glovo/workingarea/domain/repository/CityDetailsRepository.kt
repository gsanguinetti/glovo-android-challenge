package gaston.gsanguinetti.glovo.workingarea.domain.repository

import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails
import io.reactivex.Single

interface CityDetailsRepository {
    fun getCityDetails(cityCode :String) :Single<AvailableCityDetails>
}