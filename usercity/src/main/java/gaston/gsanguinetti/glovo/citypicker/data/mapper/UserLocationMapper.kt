package gaston.gsanguinetti.glovo.citypicker.data.mapper

import android.location.Location
import gaston.gsanguinetti.glovo.base.data.DataMapper
import gaston.gsanguinetti.glovo.citypicker.domain.model.UserLocation

class UserLocationMapper : DataMapper<Location, UserLocation> {

    override fun mapFromEntity(entity: Location): UserLocation =
        UserLocation(entity.latitude, entity.longitude)

    override fun mapToEntity(domainModel: UserLocation): Location {
        throw NotImplementedError()
    }
}