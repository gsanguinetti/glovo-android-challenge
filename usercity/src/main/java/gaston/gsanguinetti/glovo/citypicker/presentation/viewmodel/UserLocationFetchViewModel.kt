package gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import gaston.gsanguinetti.glovo.base.presentation.SingleLiveEvent
import gaston.gsanguinetti.glovo.base.presentation.UiEvent
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.CheckLocationPermissionUseCase
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.FetchUserCityCodeUseCase
import io.reactivex.observers.DisposableSingleObserver

class UserLocationFetchViewModel(
    private val checkLocationPermissionUseCase: CheckLocationPermissionUseCase,
    private val fetchUserCityCodeUseCase: FetchUserCityCodeUseCase
) :ViewModel() {

    val requestPermission = UiEvent()
    val locationPermissionError = UiEvent()
    val userCityCode = SingleLiveEvent<String>()

    fun startUserLocationFetch() {
        checkLocationPermissionUseCase.execute(object : DisposableSingleObserver<Boolean>() {
            @WorkerThread override fun onSuccess(hasPermission: Boolean) =
                if(!hasPermission) requestPermission.postCall() else fetchUserLocation()
            @WorkerThread override fun onError(e: Throwable) = locationPermissionError.postCall()
        })
    }

    fun onLocationPermissionGranted() = fetchUserLocation()
    fun onLocationPermissionDenied() = locationPermissionError.call()

    private fun fetchUserLocation() {
        fetchUserCityCodeUseCase.execute(object : DisposableSingleObserver<String>() {
            @WorkerThread override fun onSuccess(cityCode: String) = userCityCode.postValue(cityCode)
            @WorkerThread override fun onError(e: Throwable) = userCityCode.postValue(String())
        })
    }

    override fun onCleared() {
        fetchUserCityCodeUseCase.clear()
        checkLocationPermissionUseCase.clear()
        super.onCleared()
    }
}