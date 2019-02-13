package gaston.gsanguinetti.glovo.challenge.navigation.presentation

import androidx.lifecycle.ViewModel
import gaston.gsanguinetti.glovo.base.presentation.SingleLiveEvent
import gaston.gsanguinetti.glovo.base.presentation.UiEvent

class NavigationViewModel : ViewModel() {

    val navigateToFetchUserLocation = UiEvent()
    val navigateToCityPicker = UiEvent()
    val navigateWorkingAreaMap = SingleLiveEvent<String>()
    val finishApplication = UiEvent()

    fun startNavigation() {
        navigateToFetchUserLocation.call()
    }

    fun onUserCityCodeFetched(cityCode :String?) {
        if(cityCode.isNullOrEmpty()) navigateToCityPicker.call()
        else navigateWorkingAreaMap.value = cityCode
    }
}