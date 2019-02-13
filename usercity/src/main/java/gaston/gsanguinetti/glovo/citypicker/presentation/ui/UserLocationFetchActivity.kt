package gaston.gsanguinetti.glovo.citypicker.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import gaston.gsanguinetti.glovo.citypicker.R
import gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel.UserLocationFetchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserLocationFetchActivity : BaseLocationFetchActivity() {

    private val userLocationFetchViewModel: UserLocationFetchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location_fetch)

        userLocationFetchViewModel.run {
            startUserLocationFetch()
            requestPermission.observe(this@UserLocationFetchActivity, Observer { alertForLocationPermissionRequest() })
            locationPermissionError.observe(this@UserLocationFetchActivity, Observer { deliverUserCityResult(String()) })
            userCityCode.observe(this@UserLocationFetchActivity, Observer { deliverUserCityResult(it) })
        }
    }

    private fun alertForLocationPermissionRequest() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_location_permission)
            .setMessage(R.string.text_location_permission)
            .setPositiveButton(R.string.ok) { _, _ -> requestLocationPermission() }
            .setOnCancelListener { requestLocationPermission() }
            .create()
            .show()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    userLocationFetchViewModel.onLocationPermissionGranted()
                else
                    userLocationFetchViewModel.onLocationPermissionDenied()
            }
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 200

        fun startActivityForResult(activity: AppCompatActivity) {
            BaseLocationFetchActivity.startActivityForResult(activity, UserLocationFetchActivity::class.java)
        }
    }
}