package gaston.gsanguinetti.glovo.citypicker.presentation.model

sealed class CityPickerUiState {
    object Error : CityPickerUiState()
    object Loading : CityPickerUiState()
    class Data(val cityPickerItems: List<CityPickerItem>) : CityPickerUiState()
}