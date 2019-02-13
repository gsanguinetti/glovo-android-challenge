package gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.*
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.citypicker.domain.factory.LocationOptionsFactory
import gaston.gsanguinetti.glovo.citypicker.domain.model.LocationsToPick
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.LocationsToPickUseCase
import gaston.gsanguinetti.glovo.citypicker.presentation.factory.CityPickerItemListFactory
import gaston.gsanguinetti.glovo.citypicker.presentation.mapper.CityPickerItemMapper
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityPickerUiState
import io.reactivex.observers.DisposableSingleObserver
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityPickerViewModelTest {

    private lateinit var cityPickerViewModel: CityPickerViewModel

    private lateinit var locationsToPickUseCase: LocationsToPickUseCase
    private lateinit var cityPickerItemMapper: CityPickerItemMapper

    private lateinit var fetchLocationOptionsExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<LocationsToPick>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        locationsToPickUseCase = mock()
        cityPickerItemMapper = mock()

        cityPickerViewModel = CityPickerViewModel(
            locationsToPickUseCase,
            cityPickerItemMapper
        )

        fetchLocationOptionsExecutorCaptor = argumentCaptor()
    }

    @Test
    fun callViewModelFetchLocationOptionsExecuteUseCase() {
        cityPickerViewModel.fetchLocationOptions()
        verify(locationsToPickUseCase).execute(fetchLocationOptionsExecutorCaptor.capture(), eq(null))
    }

    @Test
    fun callViewModelFetchLocationOptionsOnStartLoadingState() {
        cityPickerViewModel.fetchLocationOptions()
        verify(locationsToPickUseCase).execute(fetchLocationOptionsExecutorCaptor.capture(), eq(null))
        fetchLocationOptionsExecutorCaptor.firstValue.onSubscribe(fetchLocationOptionsExecutorCaptor.firstValue)
        assertEquals(CityPickerUiState.Loading, cityPickerViewModel.cityPickerItemUiState.value)
    }

    @Test
    fun callViewModelFetchLocationOptionsLoadsData() {
        val numItems = randomInt(1, 10)
        val locationsToPick = LocationOptionsFactory.makeLocationOptions(numItems)
        val cityPickerItemList = CityPickerItemListFactory.makeCityPickerItemList(numItems)

        locationsToPick.countries.forEachIndexed { index, availableCountry ->
            whenever(cityPickerItemMapper.mapToEntity(availableCountry)).thenReturn(cityPickerItemList[index])
        }

        cityPickerViewModel.fetchLocationOptions()
        verify(locationsToPickUseCase).execute(fetchLocationOptionsExecutorCaptor.capture(), eq(null))
        fetchLocationOptionsExecutorCaptor.firstValue.onSuccess(locationsToPick)

        assert(cityPickerViewModel.cityPickerItemUiState.value is CityPickerUiState.Data)
        assert((cityPickerViewModel.cityPickerItemUiState.value as CityPickerUiState.Data).cityPickerItems ==
                cityPickerItemList)
    }

    @Test
    fun callViewModelFetchLocationOptionsErrorState() {
        cityPickerViewModel.fetchLocationOptions()
        verify(locationsToPickUseCase).execute(fetchLocationOptionsExecutorCaptor.capture(), eq(null))
        fetchLocationOptionsExecutorCaptor.firstValue.onError(Exception())

        assert(cityPickerViewModel.cityPickerItemUiState.value is CityPickerUiState.Error)
    }

    @Test
    fun callViewModelOnCitySelectedSetCity() {
        val cityCodeSelected = CityPickerItemListFactory.makeCityItem()
        cityPickerViewModel.onCityPicked(cityCodeSelected)
        assertEquals(cityPickerViewModel.cityCodeSelected.value, cityCodeSelected.code)
    }
}