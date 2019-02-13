package gaston.gsanguinetti.glovo.citypicker.domain.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.citypicker.domain.factory.LocationOptionsFactory
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCitiesRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCountriesRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LocationsToPickUseCaseTest {

    private lateinit var locationsToPickUseCase: LocationsToPickUseCase

    private lateinit var availableCitiesRepository: AvailableCitiesRepository
    private lateinit var availableCountriesRepository: AvailableCountriesRepository

    @Before
    fun before() {
        availableCitiesRepository = mock()
        availableCountriesRepository = mock()

        locationsToPickUseCase = LocationsToPickUseCase(availableCitiesRepository, availableCountriesRepository)
    }

    @Test
    fun buildUseCaseObservableCallsRepositories() {
        val availableCities = listOf(LocationOptionsFactory.makeAvailableCity(randomInt()))
        val availableCountries = listOf(LocationOptionsFactory.makeAvailableCountry(randomInt()))

        stubAvailableCities(availableCities)
        stubAvailableCountries(availableCountries)

        locationsToPickUseCase.buildUseCaseObservable()
            .blockingGet()

        verify(availableCitiesRepository).getAvailableCities()
        verify(availableCountriesRepository).getAvailableCountries()
    }

    @Test
    fun buildUseCaseObservableCallsRepositoryCompletes() {
        val availableCities = listOf(LocationOptionsFactory.makeAvailableCity(randomInt()))
        val availableCountries = listOf(LocationOptionsFactory.makeAvailableCountry(randomInt()))

        stubAvailableCities(availableCities)
        stubAvailableCountries(availableCountries)

        locationsToPickUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun buildUseCaseObservableGenerateValidLocationToPick() {
        val availableCountries = listOf(LocationOptionsFactory.makeAvailableCountry(randomInt()))
        val availableCities =
            listOf(LocationOptionsFactory.makeAvailableCity(randomInt(), availableCountries.first().code))

        stubAvailableCities(availableCities)
        stubAvailableCountries(availableCountries)

        locationsToPickUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertValue { it.countries.first().cities.first().code == availableCities.first().code }
    }

    private fun stubAvailableCities(availableCities: List<AvailableCity>) {
        whenever(availableCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCities))
    }

    private fun stubAvailableCountries(availableCountries: List<AvailableCountry>) {
        whenever(availableCountriesRepository.getAvailableCountries()).thenReturn(Single.just(availableCountries))
    }
}