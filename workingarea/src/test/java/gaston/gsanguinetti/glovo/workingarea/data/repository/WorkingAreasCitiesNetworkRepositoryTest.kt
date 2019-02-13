package gaston.gsanguinetti.glovo.workingarea.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import gaston.gsanguinetti.glovo.workingarea.data.api.AvailableCitiesApi
import gaston.gsanguinetti.glovo.workingarea.data.factory.CityAreaDataFactory
import gaston.gsanguinetti.glovo.workingarea.data.mapper.CityAreaMapper
import gaston.gsanguinetti.glovo.workingarea.data.model.CityArea
import gaston.gsanguinetti.glovo.workingarea.data.model.CityAreaListCacheData
import gaston.gsanguinetti.glovo.workingarea.domain.factory.AvailableCityDetailsDataFactory
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WorkingAreasCitiesNetworkRepositoryTest {

    private lateinit var workingAreaCitiesNetworkRepository: WorkingAreaCitiesNetworkRepository

    private lateinit var networkApi: NetworkApi
    private lateinit var cityAreaMapper: CityAreaMapper

    @Before
    fun before() {
        networkApi = mock()
        cityAreaMapper = mock()

        workingAreaCitiesNetworkRepository = WorkingAreaCitiesNetworkRepository(networkApi, cityAreaMapper)
    }

    @Test
    fun getWorkingAreasReturnsData() {
        val cityCode = randomString()
        val cityArea = CityAreaDataFactory.makeCityArea(cityCode)
        val availableCity = AvailableCityDetailsDataFactory.makeAvailableCity(cityCode)

        whenever(cityAreaMapper.mapFromEntity(cityArea)).thenReturn(availableCity)

        whenever(networkApi.makeCacheableApiCallForResponse(
            any(),
            any(),
            any(),
            any<(data: List<CityArea>) -> CityAreaListCacheData>(),
            any(),
            any<(api : AvailableCitiesApi) -> Single<List<CityArea>>>()
        )).thenReturn(Single.just(listOf(cityArea)))

        workingAreaCitiesNetworkRepository.getAvailableCities()
            .test()
            .await()
            .assertNoErrors()
            .assertValue(listOf(availableCity))
    }

    @Test
    fun getWorkingAreasReturnsError() {
        val exception = Exception()
        whenever(networkApi.makeCacheableApiCallForResponse(
            any(),
            any(),
            any(),
            any<(data: List<CityArea>) -> CityAreaListCacheData>(),
            any(),
            any<(api : AvailableCitiesApi) -> Single<List<CityArea>>>()
        )).thenReturn(Single.error(exception))

        workingAreaCitiesNetworkRepository.getAvailableCities()
            .test()
            .await()
            .assertError(exception)
    }
}