package gaston.gsanguinetti.glovo.workingarea.presentation.model

import com.google.android.gms.maps.model.LatLngBounds

sealed class WorkingAreaUiState {
    object Error : WorkingAreaUiState()
    object Loading : WorkingAreaUiState()
    class Data(val currentCityBounds: LatLngBounds?) : WorkingAreaUiState()
}