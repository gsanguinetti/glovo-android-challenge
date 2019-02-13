package gaston.gsanguinetti.glovo.citypicker.data.repository

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import gaston.gsanguinetti.glovo.citypicker.data.mapper.UserLocationMapper
import gaston.gsanguinetti.glovo.citypicker.domain.model.UserLocation
import gaston.gsanguinetti.glovo.citypicker.domain.repository.UserLocationRepository
import io.reactivex.Single

class UserLocationDeviceRepository(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val userLocationMapper: UserLocationMapper
) : UserLocationRepository {

    override fun fetchUserLocation(): Single<UserLocation> =
        Single.create {
            try {
                fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
                    if(task.isSuccessful) {
                        if (task.result != null) it.onSuccess(userLocationMapper.mapFromEntity(task.result!!))
                        else it.onError(Throwable("Location data is empty"))
                    } else {
                        it.onError(Throwable("Can't get the user location. Your location settings is turned off"))
                    }
                }
            } catch (ex: SecurityException) {
                it.onError(ex)
            }
        }
}