package gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import gaston.gsanguinetti.glovo.base.presentation.SingleLiveEvent
import gaston.gsanguinetti.glovo.citypicker.domain.model.LocationsToPick
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.LocationsToPickUseCase
import gaston.gsanguinetti.glovo.citypicker.presentation.mapper.CityPickerItemMapper
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityItem
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityPickerUiState
import io.reactivex.observers.DisposableSingleObserver

class CityPickerViewModel(
    private val locationsToPickUseCase: LocationsToPickUseCase,
    private val cityPickerItemMapper: CityPickerItemMapper
) : ViewModel() {

    val cityPickerItemUiState = SingleLiveEvent<CityPickerUiState>()
    val cityCodeSelected = SingleLiveEvent<String>()

    fun fetchLocationOptions() {
        locationsToPickUseCase.execute(object : DisposableSingleObserver<LocationsToPick>() {
            override fun onSuccess(locations: LocationsToPick) =
                cityPickerItemUiState.postValue(
                    CityPickerUiState.Data(locations.countries.map { cityPickerItemMapper.mapToEntity(it) })
                )
            override fun onError(e: Throwable) = cityPickerItemUiState.postValue(CityPickerUiState.Error)
            override fun onStart() = cityPickerItemUiState.postValue(CityPickerUiState.Loading)
        })
    }

    fun onCityPicked(city: CityItem) {
        cityCodeSelected.value = city.code
    }

    override fun onCleared() {
        locationsToPickUseCase.clear()
        super.onCleared()
    }
}