package gaston.gsanguinetti.glovo.workingarea.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import gaston.gsanguinetti.glovo.base.presentation.SingleLiveEvent
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails
import gaston.gsanguinetti.glovo.workingarea.domain.model.MapContent
import gaston.gsanguinetti.glovo.workingarea.domain.usecase.CityDetailsUseCase
import gaston.gsanguinetti.glovo.workingarea.domain.usecase.MapContentUseCase
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.CityDetailsContentMapper
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.MapPresentationContentMapper
import gaston.gsanguinetti.glovo.workingarea.presentation.model.CityDetailsUiState
import gaston.gsanguinetti.glovo.workingarea.presentation.model.MapPresentationContent
import gaston.gsanguinetti.glovo.workingarea.presentation.model.WorkingAreaUiState
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver

class WorkingAreaViewModel(
    private val mapContentUseCase: MapContentUseCase,
    private val cityDetailsUseCase: CityDetailsUseCase,
    private val mapPresentationContentMapper: MapPresentationContentMapper,
    private val cityDetailsContentMapper: CityDetailsContentMapper
) : ViewModel() {

    private lateinit var userCity: String
    private var currentFocusedLatLng: LatLng? = null

    private var mapPresentationContent: MapPresentationContent? = null

    private var currentMapZoomLevel: Int = -1

    val displayCityMarkers = SingleLiveEvent<List<MarkerOptions>>()
    val displayCityPolygons = SingleLiveEvent<List<PolygonOptions>>()
    val focusOnBounds = SingleLiveEvent<LatLngBounds>()

    val workingAreaUiState = MutableLiveData<WorkingAreaUiState>()
    val cityDetailsUiState = MutableLiveData<CityDetailsUiState>()

    fun startWorkingArea(userCity: String) {
        this.userCity = userCity

        mapContentUseCase.execute(object : DisposableSingleObserver<MapContent>() {
            override fun onSuccess(mapContent: MapContent) {
                mapPresentationContent = mapPresentationContentMapper.mapToEntity(mapContent)
                workingAreaUiState.postValue(WorkingAreaUiState.Data(getLatLngBoundsForCityCode(userCity)))
            }

            override fun onError(e: Throwable) = workingAreaUiState.postValue(WorkingAreaUiState.Error)
            override fun onStart() = workingAreaUiState.postValue(WorkingAreaUiState.Loading)
        })
    }

    fun onMapMoved(zoomLevel: Int, centerLatLng: LatLng) {
        mapPresentationContent?.run {
            if (needReplaceMapDrawings(currentMapZoomLevel, zoomLevel))
                if (zoomLevel < MAX_ZOOM_LEVEL_CLUSTER_CITY_POLYGONS) displayCityMarkers.value = cityMarkers
                else displayCityPolygons.value = citiesPolygons
        }
        currentFocusedLatLng = centerLatLng
        currentMapZoomLevel = zoomLevel
        obtainCityDetailsForLocation(centerLatLng)
    }

    fun onRetryMapMovedRequest() {
        currentFocusedLatLng?.let { onMapMoved(currentMapZoomLevel, it) }
    }

    fun onCityMarkerClick(cityCode: String?) {
        focusOnBounds.value = mapPresentationContent?.cityLatLngBounds?.find { it.first == cityCode }?.second
    }

    private fun needReplaceMapDrawings(currentZoom: Int, newZoom: Int): Boolean =
        if (currentZoom == -1) true
        else (currentZoom - MAX_ZOOM_LEVEL_CLUSTER_CITY_POLYGONS) *
                (newZoom - MAX_ZOOM_LEVEL_CLUSTER_CITY_POLYGONS) <= 0

    private fun obtainCityDetailsForLocation(latLng: LatLng) {
        cityDetailsUseCase.clear()
        cityDetailsUseCase.execute(object : DisposableMaybeObserver<AvailableCityDetails>() {
            override fun onSuccess(availableCityDetails: AvailableCityDetails) =
                cityDetailsUiState.postValue(
                    CityDetailsUiState.Data(cityDetailsContentMapper.mapToEntity(availableCityDetails))
                )

            override fun onComplete() = cityDetailsUiState.postValue(CityDetailsUiState.NotAvailable)
            override fun onError(e: Throwable) = cityDetailsUiState.postValue(CityDetailsUiState.Error)
            override fun onStart() = cityDetailsUiState.postValue(CityDetailsUiState.Loading)
        }, latLng)
    }

    private fun getLatLngBoundsForCityCode(cityCode: String): LatLngBounds? =
        mapPresentationContent?.cityLatLngBounds?.find { it.first == cityCode }?.second

    override fun onCleared() {
        mapContentUseCase.clear()
        cityDetailsUseCase.clear()
        super.onCleared()
    }

    companion object {
        private const val MAX_ZOOM_LEVEL_CLUSTER_CITY_POLYGONS = 10
    }
}