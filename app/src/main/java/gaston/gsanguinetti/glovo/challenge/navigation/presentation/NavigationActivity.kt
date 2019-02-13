package gaston.gsanguinetti.glovo.challenge.navigation.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import gaston.gsanguinetti.glovo.base.presentation.nonNullObserve
import gaston.gsanguinetti.glovo.citypicker.presentation.ui.BaseLocationFetchActivity.Companion.getCityCodeFromResult
import gaston.gsanguinetti.glovo.citypicker.presentation.ui.BaseLocationFetchActivity.Companion.isRequestCanceled
import gaston.gsanguinetti.glovo.citypicker.presentation.ui.BaseLocationFetchActivity.Companion.isUserLocationRequestResult
import gaston.gsanguinetti.glovo.citypicker.presentation.ui.CityPickerActivity
import gaston.gsanguinetti.glovo.citypicker.presentation.ui.UserLocationFetchActivity
import gaston.gsanguinetti.glovo.workingarea.presentation.ui.WorkingAreaActivity
import gaston.gsanguinetti.glovo.workingarea.presentation.ui.WorkingAreaActivity.Companion.isWorkingAreaRequestCode
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationActivity : AppCompatActivity() {

    private val navigationViewModel: NavigationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigationViewModel.run {
            if(savedInstanceState == null) startNavigation()
            navigateToFetchUserLocation.observe(this@NavigationActivity, Observer { navigateToFetchUserLocation() })
            navigateToCityPicker.observe(this@NavigationActivity, Observer { navigateToCityPicker() })
            finishApplication.observe(this@NavigationActivity, Observer { finish() })
            navigateWorkingAreaMap.nonNullObserve(this@NavigationActivity) { navigateToWorkingArea(it) }
        }
    }

    private fun navigateToFetchUserLocation() {
        UserLocationFetchActivity.startActivityForResult(this)
    }

    private fun navigateToCityPicker() {
        CityPickerActivity.startActivityForResult(this)
    }

    private fun navigateToWorkingArea(currentCityCode: String) {
        WorkingAreaActivity.startActivityForResult(this, currentCityCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            isUserLocationRequestResult(requestCode) -> {
                if (isRequestCanceled(resultCode)) finish()
                else navigationViewModel.onUserCityCodeFetched(getCityCodeFromResult(resultCode, data))
            }
            isWorkingAreaRequestCode(requestCode) -> {
                // Application should finish
                finish()
            }
        }
    }
}