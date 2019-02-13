package gaston.gsanguinetti.glovo.workingarea.presentation.model

sealed class CityDetailsUiState {
    object Error : CityDetailsUiState()
    object Loading : CityDetailsUiState()
    object NotAvailable : CityDetailsUiState()
    class Data(val cityDetailsContent: CityDetailsContent) : CityDetailsUiState()
}