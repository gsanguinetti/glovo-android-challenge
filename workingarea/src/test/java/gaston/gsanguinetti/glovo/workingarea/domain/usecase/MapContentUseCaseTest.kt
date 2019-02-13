package gaston.gsanguinetti.glovo.workingarea.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import gaston.gsanguinetti.glovo.base.test.randomDouble
import gaston.gsanguinetti.glovo.workingarea.domain.factory.AvailableCityDetailsDataFactory
import gaston.gsanguinetti.glovo.workingarea.domain.repository.WorkingAreaCitiesRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MapContentUseCaseTest {

    private lateinit var mapContentUseCase: MapContentUseCase

    private lateinit var availableCitiesRepository: WorkingAreaCitiesRepository
    private lateinit var polygonUtil: PolygonUtil


    @Before
    fun before() {
        availableCitiesRepository = mock()
        polygonUtil = mock()

        mapContentUseCase = MapContentUseCase(availableCitiesRepository, polygonUtil)
    }

    @Test
    fun buildUseCaseObservableCallsRepository() {
        val availableCities = AvailableCityDetailsDataFactory.makeAvailableCities()
        whenever(availableCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCities))
        whenever(polygonUtil.decode(any())).thenReturn(listOf(LatLng(randomDouble(), randomDouble())))

        mapContentUseCase.buildUseCaseObservable().blockingGet()
        verify(availableCitiesRepository).getAvailableCities()
    }

    @Test
    fun buildUseCaseObservableCallsReturnsData() {
        val availableCities = AvailableCityDetailsDataFactory.makeAvailableCities()
        whenever(availableCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCities))
        whenever(polygonUtil.decode(any())).thenReturn(listOf(LatLng(randomDouble(), randomDouble())))

        mapContentUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertNoErrors()
            .assertValue { it.cityLatLngBounds.size == availableCities.size }
            .assertValue { it.cityMapPoints.size == availableCities.size }
    }

    @Test
    fun buildUseCaseObservableCallsError() {
        val exception = Exception()
        whenever(availableCitiesRepository.getAvailableCities()).thenReturn(Single.error(exception))
        mapContentUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertError(exception)
            .assertNoValues()
    }
}