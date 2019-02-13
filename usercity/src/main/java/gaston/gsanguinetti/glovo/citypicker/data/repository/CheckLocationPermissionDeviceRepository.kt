package gaston.gsanguinetti.glovo.citypicker.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import gaston.gsanguinetti.glovo.citypicker.domain.repository.CheckLocationPermissionRepository
import io.reactivex.Single

class CheckLocationPermissionDeviceRepository(
    private val context: Context,
    private val permissionChecker: PermissionChecker
) : CheckLocationPermissionRepository {

    override fun hasLocationPermissionGranted(): Single<Boolean> =
        Single.just(checkLocationPermission())

    private fun checkLocationPermission(): Boolean =
        permissionChecker.checkPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}

class PermissionChecker {

    fun checkPermission(context: Context, permission: String): Int =
        ContextCompat.checkSelfPermission(context, permission)
}