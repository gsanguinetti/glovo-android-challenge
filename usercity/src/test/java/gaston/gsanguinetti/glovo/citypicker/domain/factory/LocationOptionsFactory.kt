package gaston.gsanguinetti.glovo.citypicker.domain.factory

import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.base.test.randomStringList
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.citypicker.domain.model.AvailableCountry
import gaston.gsanguinetti.glovo.citypicker.domain.model.LocationsToPick

class LocationOptionsFactory {
    companion object {
        fun makeLocationOptions(numItems: Int) =
            LocationsToPick((0 until numItems).map { makeAvailableCountry(numItems) })

        fun makeAvailableCountry(numCities: Int) =
            AvailableCountry(
                randomString(),
                randomString(),
                (0 until numCities).map { makeAvailableCity(numCities) }
            )

        fun makeAvailableCity(numPaths :Int, countryCode :String? = null) =
            AvailableCity(
                randomString(),
                randomStringList(numPaths),
                countryCode ?: randomString(),
                randomString()
            )
    }
}