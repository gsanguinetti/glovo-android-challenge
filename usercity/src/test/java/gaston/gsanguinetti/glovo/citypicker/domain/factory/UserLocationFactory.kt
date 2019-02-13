package gaston.gsanguinetti.glovo.citypicker.domain.factory

import gaston.gsanguinetti.glovo.base.test.randomDouble
import gaston.gsanguinetti.glovo.citypicker.domain.model.UserLocation

class UserLocationFactory {
    companion object {
        fun makeUserLocation() = UserLocation(randomDouble(), randomDouble())
    }
}