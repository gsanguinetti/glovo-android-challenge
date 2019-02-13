package gaston.gsanguinetti.glovo.citypicker.domain.usecase

import gaston.gsanguinetti.glovo.base.domain.SingleUseCase
import gaston.gsanguinetti.glovo.citypicker.domain.model.LocationsToPick
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCitiesRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCountriesRepository
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles

class LocationsToPickUseCase(
    private val availableCitiesRepository: AvailableCitiesRepository,
    private val availableCountriesRepository: AvailableCountriesRepository
) : SingleUseCase<LocationsToPick, Unit>() {

    public override fun buildUseCaseObservable(params: Unit?): Single<LocationsToPick> =
        Singles.zip(
            availableCitiesRepository.getAvailableCities().setUpForUseCase(),
            availableCountriesRepository.getAvailableCountries().setUpForUseCase()
        ) { cities, countries ->
            countries.forEach { country ->
                country.cities = cities.filter { it.countryCode == country.code }
                    .sortedBy { it.name }
            }
            LocationsToPick(countries.sortedBy { it.name })
        }.setUpForUseCase()
}