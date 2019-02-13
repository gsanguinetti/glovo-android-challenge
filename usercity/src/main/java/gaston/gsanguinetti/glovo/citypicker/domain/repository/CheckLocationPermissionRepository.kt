package gaston.gsanguinetti.glovo.citypicker.domain.repository

import io.reactivex.Single

interface CheckLocationPermissionRepository {
    fun hasLocationPermissionGranted() : Single<Boolean>
}