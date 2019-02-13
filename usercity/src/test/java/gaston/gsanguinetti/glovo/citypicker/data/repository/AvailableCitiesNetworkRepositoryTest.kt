package gaston.gsanguinetti.glovo.citypicker.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.citypicker.data.api.AvailableLocationsApi
import gaston.gsanguinetti.glovo.citypicker.data.factory.CityPickerDataFactory
import gaston.gsanguinetti.glovo.citypicker.data.mapper.AvailableCityMapper
import gaston.gsanguinetti.glovo.citypicker.data.model.AvailableCitiesCacheData
import gaston.gsanguinetti.glovo.citypicker.data.model.City
import gaston.gsanguinetti.glovo.citypicker.domain.factory.LocationOptionsFactory
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AvailableCitiesNetworkRepositoryTest {

    private lateinit var availableCitiesNetworkRepository: AvailableCitiesNetworkRepository

    private lateinit var availableCityMapper: AvailableCityMapper
    private lateinit var networkApi: NetworkApi

    @Before
    fun before() {
        availableCityMapper = mock()
        networkApi = mock()

        availableCitiesNetworkRepository = AvailableCitiesNetworkRepository(availableCityMapper, networkApi)
    }

    @Test
    fun getAvailableCitiesReturnsData() {
        val citiesSize = randomInt(1, 5)
        val cities = mutableListOf<City>().apply { repeat(citiesSize) { add(CityPickerDataFactory.makeCity()) } }
        val availableCities =
            mutableListOf<AvailableCity>().apply {
                repeat(citiesSize) {
                    add(LocationOptionsFactory.makeAvailableCity(2))
                }
            }
        (0 until citiesSize).forEach {
            whenever(availableCityMapper.mapFromEntity(cities[it])).thenReturn(
                availableCities[it]
            )
        }
        whenever(
            networkApi.makeCacheableApiCallForResponse(
                any(),
                any(),
                any(),
                any<((data :List<City>?) -> AvailableCitiesCacheData)>(),
                any(),
                any<((api :AvailableLocationsApi) -> Single<List<City>>)>())
        ).thenReturn(Single.just(cities))

        availableCitiesNetworkRepository.getAvailableCities()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(availableCities)
    }

    @Test
    fun getAvailableCitiesError() {
        val exception = Exception()
        whenever(
            networkApi.makeCacheableApiCallForResponse(
                any(),
                any(),
                any(),
                any<((data :List<City>?) -> AvailableCitiesCacheData)>(),
                any(),
                any<((api :AvailableLocationsApi) -> Single<List<City>>)>())
        ).thenReturn(Single.error(exception))

        availableCitiesNetworkRepository.getAvailableCities()
            .test()
            .await()
            .assertError(exception)
    }
}