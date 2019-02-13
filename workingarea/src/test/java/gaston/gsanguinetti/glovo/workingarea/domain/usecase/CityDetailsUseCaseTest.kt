package gaston.gsanguinetti.glovo.workingarea.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.*
import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import gaston.gsanguinetti.glovo.base.test.randomDouble
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.domain.factory.AvailableCityDetailsDataFactory
import gaston.gsanguinetti.glovo.workingarea.domain.repository.CityDetailsRepository
import gaston.gsanguinetti.glovo.workingarea.domain.repository.WorkingAreaCitiesRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityDetailsUseCaseTest {

    private lateinit var cityDetailsUseCase: CityDetailsUseCase

    private lateinit var workingAreaCitiesRepository: WorkingAreaCitiesRepository
    private lateinit var cityDetailsRepository: CityDetailsRepository
    private lateinit var polygonUtil: PolygonUtil

    @Before
    fun before() {
        workingAreaCitiesRepository = mock()
        cityDetailsRepository = mock()
        polygonUtil = mock()

        cityDetailsUseCase = CityDetailsUseCase(workingAreaCitiesRepository, cityDetailsRepository, polygonUtil)
    }

    @Test
    fun buildUseCaseObservableCallsRepositories() {
        val cityCode = randomString()
        val focusedLatLng = LatLng(randomDouble(), randomDouble())
        val availableCities = AvailableCityDetailsDataFactory.makeAvailableCities(cityCode)
        val availableCityDetails = AvailableCityDetailsDataFactory.makeAvailableCityDetails(cityCode)

        whenever(workingAreaCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCities))
        whenever(cityDetailsRepository.getCityDetails(any())).thenReturn(Single.just(availableCityDetails))
        whenever(polygonUtil.decode(any())).thenReturn(listOf(LatLng(randomDouble(), randomDouble())))
        whenever(polygonUtil.containsLocation(eq(focusedLatLng), any())).thenReturn(true)

        cityDetailsUseCase.buildUseCaseObservable(focusedLatLng)
            .blockingGet()
        verify(workingAreaCitiesRepository).getAvailableCities()
        verify(cityDetailsRepository).getCityDetails(any())
    }

    @Test
    fun buildUseCaseObservableReturnsData() {
        val cityCode = randomString()
        val focusedLatLng = LatLng(randomDouble(), randomDouble())
        val availableCities = AvailableCityDetailsDataFactory.makeAvailableCities(cityCode)
        val availableCityDetails = AvailableCityDetailsDataFactory.makeAvailableCityDetails(cityCode)

        whenever(workingAreaCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCities))
        whenever(cityDetailsRepository.getCityDetails(cityCode)).thenReturn(Single.just(availableCityDetails))

        availableCities.forEach { city ->
            city.workingArea.forEach {

                val coordinateToStub =
                    if (city.code == cityCode) focusedLatLng
                    else {
                        val coordinates = LatLng(randomDouble(), randomDouble())
                        stubLocationSearch(focusedLatLng, listOf(coordinates), false)
                        coordinates
                    }

                whenever(polygonUtil.decode(it)).thenReturn(listOf(coordinateToStub))
            }
        }

        stubLocationSearch(focusedLatLng, listOf(focusedLatLng), true)

        cityDetailsUseCase.buildUseCaseObservable(focusedLatLng)
            .test()
            .await()
            .assertNoErrors()
            .assertValue(availableCityDetails)
    }

    @Test
    fun buildUseCaseObservableWithNoCityFound() {
        val focusedLatLng = LatLng(randomDouble(), randomDouble())
        val availableCities = AvailableCityDetailsDataFactory.makeAvailableCities()

        whenever(workingAreaCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCities))

        whenever(polygonUtil.containsLocation(any(), any())).thenReturn(false)
        whenever(polygonUtil.decode(any())).thenReturn(listOf(LatLng(randomDouble(), randomDouble())))

        stubLocationSearch(focusedLatLng, listOf(focusedLatLng), false)

        cityDetailsUseCase.buildUseCaseObservable(focusedLatLng)
            .test()
            .await()
            .assertComplete()
            .assertNoValues()
    }

    private fun stubLocationSearch(location: LatLng, coordinates: List<LatLng>, mustFind: Boolean) =
        whenever(polygonUtil.containsLocation(location, coordinates)).thenReturn(mustFind)
}