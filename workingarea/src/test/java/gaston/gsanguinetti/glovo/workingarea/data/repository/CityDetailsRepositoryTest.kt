package gaston.gsanguinetti.glovo.workingarea.data.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.network.datasource.api.NetworkApi
import gaston.gsanguinetti.glovo.workingarea.data.api.AvailableCitiesApi
import gaston.gsanguinetti.glovo.workingarea.data.factory.CityDetailsDataFactory
import gaston.gsanguinetti.glovo.workingarea.data.mapper.CityDetailsMapper
import gaston.gsanguinetti.glovo.workingarea.data.model.CityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.factory.AvailableCityDetailsDataFactory
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityDetailsRepositoryTest {

    private lateinit var cityDetailsNetworkRepository: CityDetailsNetworkRepository

    private lateinit var networkApi: NetworkApi
    private lateinit var cityDetailsMapper: CityDetailsMapper

    @Before
    fun before() {
        networkApi = mock()
        cityDetailsMapper = mock()

        cityDetailsNetworkRepository = CityDetailsNetworkRepository(networkApi, cityDetailsMapper)
    }

    @Test
    fun getCityDetailsReturnsData() {
        val cityCode = randomString()
        val cityDetails = CityDetailsDataFactory.makeCityDetails(cityCode)
        val availableCityDetails = AvailableCityDetailsDataFactory.makeAvailableCityDetails(cityCode)

        whenever(networkApi.makeCacheableApiCallForResponse(
            any(),
            any(),
            any(),
            any<(api :AvailableCitiesApi) -> Single<CityDetails>>()
        )).thenReturn(Single.just(cityDetails))
        whenever(cityDetailsMapper.mapFromEntity(cityDetails)).thenReturn(availableCityDetails)

        cityDetailsNetworkRepository.getCityDetails(cityCode)
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(availableCityDetails)
    }

    @Test
    fun getCityDetailsError() {
        val exception = Exception()
        whenever(networkApi.makeCacheableApiCallForResponse(
            any(),
            any(),
            any(),
            any<(api :AvailableCitiesApi) -> Single<CityDetails>>()
        )).thenReturn(Single.error(exception))

        cityDetailsNetworkRepository.getCityDetails(randomString())
            .test()
            .await()
            .assertError(exception)
    }
}