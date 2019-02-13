package gaston.gsanguinetti.glovo.citypicker.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import gaston.gsanguinetti.glovo.citypicker.domain.factory.LocationOptionsFactory
import gaston.gsanguinetti.glovo.citypicker.domain.factory.PolygonCoordinateListFactory
import gaston.gsanguinetti.glovo.citypicker.domain.factory.UserLocationFactory
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.citypicker.domain.model.UserLocation
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCitiesRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.UserLocationRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FetchUserCityCodeUseCaseTest {

    private lateinit var fetchUserCityCodeUseCase: FetchUserCityCodeUseCase

    private lateinit var userLocationRepository: UserLocationRepository
    private lateinit var availableCitiesRepository: AvailableCitiesRepository
    private lateinit var polygonUtil: PolygonUtil

    @Before
    fun before() {
        userLocationRepository = mock()
        availableCitiesRepository = mock()
        polygonUtil = mock()
        fetchUserCityCodeUseCase =
            FetchUserCityCodeUseCase(userLocationRepository, availableCitiesRepository, polygonUtil)
    }

    @Test
    fun buildUseCaseObservableCallsRepositories() {
        val userLocation = UserLocationFactory.makeUserLocation()
        val availableCities = mutableListOf<AvailableCity>()
        repeat(3) { availableCities.add(LocationOptionsFactory.makeAvailableCity(3)) }
        val polygonCoordinates = PolygonCoordinateListFactory.makePolyconCoordinateList(3)

        stubUserLocationResponse(userLocation)
        stubAvailableCitiesResponse(availableCities)
        availableCities.forEach { it.workingArea.forEach { stubPolygonDecoder(it, polygonCoordinates) } }

        fetchUserCityCodeUseCase.buildUseCaseObservable()
            .blockingGet()
        verify(userLocationRepository).fetchUserLocation()
        verify(availableCitiesRepository).getAvailableCities()
    }

    @Test
    fun buildUseCaseObservableCallsRepositoryCompletes() {
        val userLocation = UserLocationFactory.makeUserLocation()
        val availableCities = mutableListOf<AvailableCity>()
        repeat(3) { availableCities.add(LocationOptionsFactory.makeAvailableCity(3)) }
        val polygonCoordinates = PolygonCoordinateListFactory.makePolyconCoordinateList(3)

        stubUserLocationResponse(userLocation)
        stubAvailableCitiesResponse(availableCities)
        availableCities.forEach { it.workingArea.forEach { stubPolygonDecoder(it, polygonCoordinates) } }

        fetchUserCityCodeUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun buildUseCaseObservableFindUserCity() {
        val userLocation = UserLocationFactory.makeUserLocation()
        val availableCities = mutableListOf(LocationOptionsFactory.makeAvailableCity(3))
        val polygonCoordinates = PolygonCoordinateListFactory.makePolyconCoordinateList(3)

        stubUserLocationResponse(userLocation)
        stubAvailableCitiesResponse(availableCities)
        stubPolygonFinder(polygonCoordinates, LatLng(userLocation.latitude, userLocation.longitude))
        availableCities.forEach { it.workingArea.forEach { stubPolygonDecoder(it, polygonCoordinates) } }

        fetchUserCityCodeUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertValue { it == availableCities[0].code }
    }

    private fun stubUserLocationResponse(userLocation: UserLocation) {
        whenever(userLocationRepository.fetchUserLocation()).thenReturn(Single.just(userLocation))
    }

    private fun stubAvailableCitiesResponse(availableCitiesCacheData: List<AvailableCity>) {
        whenever(availableCitiesRepository.getAvailableCities()).thenReturn(Single.just(availableCitiesCacheData))
    }

    private fun stubPolygonDecoder(encodedPath: String, polygonCoordinates: List<LatLng>) {
        whenever(polygonUtil.decode(encodedPath)).thenReturn(polygonCoordinates)
    }

    private fun stubPolygonFinder(polygonCoordinates: List<LatLng>, location: LatLng) {
        whenever(polygonUtil.containsLocation(location, polygonCoordinates)).thenReturn(true)
    }
}