package gaston.gsanguinetti.glovo.citypicker.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.citypicker.data.api.AvailableLocationsApi
import gaston.gsanguinetti.glovo.citypicker.data.factory.CityPickerDataFactory
import gaston.gsanguinetti.glovo.citypicker.data.mapper.AvailableCountryMapper
import gaston.gsanguinetti.glovo.citypicker.data.model.AvailableCountriesCacheData
import gaston.gsanguinetti.glovo.citypicker.data.model.Country
import gaston.gsanguinetti.glovo.citypicker.domain.factory.LocationOptionsFactory
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry
import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AvailableCountriesNetworkRepositoryTest {

    private lateinit var availableCountriesNetworkRepository: AvailableCountriesNetworkRepository

    private lateinit var availableCountryMapper: AvailableCountryMapper
    private lateinit var networkApi: NetworkApi

    @Before
    fun before() {
        availableCountryMapper = mock()
        networkApi = mock()

        availableCountriesNetworkRepository = AvailableCountriesNetworkRepository(availableCountryMapper, networkApi)
    }

    @Test
    fun getAvailableCountriesReturnsData() {
        val countriesSize = randomInt(1, 5)
        val countries = mutableListOf<Country>().apply { repeat(countriesSize) { add(CityPickerDataFactory.makeCountry()) } }
        val availableCountries =
            mutableListOf<AvailableCountry>().apply {
                repeat(countriesSize) {
                    add(LocationOptionsFactory.makeAvailableCountry(0))
                }
            }
        (0 until countriesSize).forEach {
            whenever(availableCountryMapper.mapFromEntity(countries[it])).thenReturn(
                availableCountries[it]
            )
        }
        whenever(
            networkApi.makeCacheableApiCallForResponse(
                any(),
                any(),
                any(),
                any<((data :List<Country>?) -> AvailableCountriesCacheData)>(),
                any(),
                any<((api : AvailableLocationsApi) -> Single<List<Country>>)>()
            )
        ).thenReturn(Single.just(countries))

        availableCountriesNetworkRepository.getAvailableCountries()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(availableCountries)
    }

    @Test
    fun getAvailableCountriesError() {
        val exception = Exception()
        whenever(
            networkApi.makeCacheableApiCallForResponse(
                any(),
                any(),
                any(),
                any<((data :List<Country>?) -> AvailableCountriesCacheData)>(),
                any(),
                any<((api : AvailableLocationsApi) -> Single<List<Country>>)>()
            )
        ).thenReturn(Single.error(exception))

        availableCountriesNetworkRepository.getAvailableCountries()
            .test()
            .await()
            .assertError(exception)
    }
}