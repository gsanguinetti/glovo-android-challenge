package gaston.gsanguinetti.glovo.citypicker.data.factory

import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.base.test.randomStringList
import gaston.gsanguinetti.glovo.citypicker.data.model.City
import gaston.gsanguinetti.glovo.citypicker.data.model.Country

class CityPickerDataFactory {
    companion object {
        fun makeCity() =
            City(
                randomString(),
                randomString(),
                randomString(),
                randomStringList(randomInt())
            )

        fun makeCountry() = Country(randomString(), randomString())
    }
}