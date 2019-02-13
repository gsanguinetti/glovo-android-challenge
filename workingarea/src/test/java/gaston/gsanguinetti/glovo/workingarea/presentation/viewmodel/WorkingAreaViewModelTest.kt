package gaston.gsanguinetti.glovo.workingarea.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.nhaarman.mockito_kotlin.*
import gaston.gsanguinetti.glovo.base.test.randomDouble
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.domain.factory.AvailableCityDetailsDataFactory
import gaston.gsanguinetti.glovo.workingarea.domain.factory.MapContentDataFactory
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.model.MapContent
import gaston.gsanguinetti.glovo.workingarea.domain.usecase.CityDetailsUseCase
import gaston.gsanguinetti.glovo.workingarea.domain.usecase.MapContentUseCase
import gaston.gsanguinetti.glovo.workingarea.presentation.factory.MapPresentationContentDataFactory
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.CityDetailsContentMapper
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.MapPresentationContentMapper
import gaston.gsanguinetti.glovo.workingarea.presentation.model.CityDetailsUiState
import gaston.gsanguinetti.glovo.workingarea.presentation.model.WorkingAreaUiState
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WorkingAreaViewModelTest {

    private lateinit var workingAreaViewModel: WorkingAreaViewModel

    private lateinit var mapContentUseCase: MapContentUseCase
    private lateinit var cityDetailsUseCase: CityDetailsUseCase
    private lateinit var mapPresentationContentMapper: MapPresentationContentMapper
    private lateinit var cityDetailsContentMapper: CityDetailsContentMapper

    private lateinit var mapContentExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<MapContent>>
    private lateinit var cityDetailsExecutorCaptor: KArgumentCaptor<DisposableMaybeObserver<AvailableCityDetails>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        mapContentUseCase = mock()
        cityDetailsUseCase = mock()
        mapPresentationContentMapper = mock()
        cityDetailsContentMapper = mock()

        workingAreaViewModel = WorkingAreaViewModel(
            mapContentUseCase,
            cityDetailsUseCase,
            mapPresentationContentMapper,
            cityDetailsContentMapper
        )

        mapContentExecutorCaptor = argumentCaptor()
        cityDetailsExecutorCaptor = argumentCaptor()
    }

    @Test
    fun callViewModelFetchMapContentExecuteUseCase() {
        workingAreaViewModel.startWorkingArea(randomString())
        verify(mapContentUseCase).execute(mapContentExecutorCaptor.capture(), eq(null))
    }

    @Test
    fun callViewModelFetchMapContentOnStartLoadingState() {
        workingAreaViewModel.startWorkingArea(randomString())
        verify(mapContentUseCase).execute(mapContentExecutorCaptor.capture(), eq(null))
        mapContentExecutorCaptor.firstValue.onSubscribe(mapContentExecutorCaptor.firstValue)
        assertEquals(WorkingAreaUiState.Loading, workingAreaViewModel.workingAreaUiState.value)
    }

    @Test
    fun callViewModelFetchMapContentLoadsData() {
        val initialCityCode = randomString()
        val mapContent = MapContentDataFactory.makeMapContent(initialCityCode)
        val mapPresentationContent = MapPresentationContentDataFactory.makeMapPresentationContent(initialCityCode)

        whenever(mapPresentationContentMapper.mapToEntity(mapContent)).thenReturn(mapPresentationContent)

        workingAreaViewModel.startWorkingArea(initialCityCode)
        verify(mapContentUseCase).execute(mapContentExecutorCaptor.capture(), eq(null))
        mapContentExecutorCaptor.firstValue.onSuccess(mapContent)

        //Initial city focus assertions
        assert(workingAreaViewModel.workingAreaUiState.value is WorkingAreaUiState.Data)
        assertEquals(
            mapPresentationContent.cityLatLngBounds.find { it.first == initialCityCode }!!.second,
            (workingAreaViewModel.workingAreaUiState.value as WorkingAreaUiState.Data).currentCityBounds
        )

        //Zoom-in on city and assert for polygons displayed on map
        val cityPolygonsObserver: Observer<List<PolygonOptions>> = mock()
        workingAreaViewModel.displayCityPolygons.observeForever(cityPolygonsObserver)
        workingAreaViewModel.onMapMoved(18, LatLng(randomDouble(), randomDouble()))
        verify(cityPolygonsObserver).onChanged(mapPresentationContent.citiesPolygons)

        //Zoom-out and assert for markers displayed on map
        val cityMarkerObserver: Observer<List<MarkerOptions>> = mock()
        workingAreaViewModel.displayCityMarkers.observeForever(cityMarkerObserver)
        workingAreaViewModel.onMapMoved(2, LatLng(randomDouble(), randomDouble()))
        verify(cityMarkerObserver).onChanged(mapPresentationContent.cityMarkers)
    }

    @Test
    fun callViewModelFetchMapContentError() {
        workingAreaViewModel.startWorkingArea(randomString())
        verify(mapContentUseCase).execute(mapContentExecutorCaptor.capture(), eq(null))
        mapContentExecutorCaptor.firstValue.onError(Exception())

        assertEquals(WorkingAreaUiState.Error, workingAreaViewModel.workingAreaUiState.value)
    }

    @Test
    fun callViewModelFetchCityDetailsLoadsData() {
        val locationFocused = LatLng(randomDouble(), randomDouble())
        val availableCityDetails = AvailableCityDetailsDataFactory.makeAvailableCityDetails()
        val cityDetailsContent = MapPresentationContentDataFactory.makeCityDetailsContent()

        whenever(cityDetailsContentMapper.mapToEntity(availableCityDetails)).thenReturn(cityDetailsContent)
        workingAreaViewModel.onMapMoved(randomInt(), locationFocused)

        verify(cityDetailsUseCase).execute(cityDetailsExecutorCaptor.capture(), eq(locationFocused))
        cityDetailsExecutorCaptor.firstValue.onSuccess(availableCityDetails)

        assert(workingAreaViewModel.cityDetailsUiState.value is CityDetailsUiState.Data)
        assertEquals((workingAreaViewModel.cityDetailsUiState.value as CityDetailsUiState.Data).cityDetailsContent,
            cityDetailsContent)
    }

    @Test
    fun callViewModelFetchCityDetailsOutsideBounds() {
        val locationFocused = LatLng(randomDouble(), randomDouble())
        workingAreaViewModel.onMapMoved(randomInt(), locationFocused)
        verify(cityDetailsUseCase).execute(cityDetailsExecutorCaptor.capture(), eq(locationFocused))
        cityDetailsExecutorCaptor.firstValue.onComplete()
        assertEquals(workingAreaViewModel.cityDetailsUiState.value, CityDetailsUiState.NotAvailable)
    }

    @Test
    fun callViewModelFetchCityDetailsError() {
        val locationFocused = LatLng(randomDouble(), randomDouble())
        workingAreaViewModel.onMapMoved(randomInt(), locationFocused)
        verify(cityDetailsUseCase).execute(cityDetailsExecutorCaptor.capture(), eq(locationFocused))
        cityDetailsExecutorCaptor.firstValue.onError(Exception())
        assertEquals(workingAreaViewModel.cityDetailsUiState.value, CityDetailsUiState.Error)
    }
}