package gaston.gsanguinetti.glovo.citypicker.domain.repository

import gaston.gsanguinetti.glovo.citypicker.domain.model.UserLocation
import io.reactivex.Single

interface UserLocationRepository {
    fun fetchUserLocation() : Single<UserLocation>
}