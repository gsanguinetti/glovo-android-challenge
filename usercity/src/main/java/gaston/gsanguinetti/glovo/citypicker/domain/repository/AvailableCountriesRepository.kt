package gaston.gsanguinetti.glovo.citypicker.domain.repository

import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry
import io.reactivex.Single

interface AvailableCountriesRepository {
    fun getAvailableCountries(): Single<List<AvailableCountry>>
}