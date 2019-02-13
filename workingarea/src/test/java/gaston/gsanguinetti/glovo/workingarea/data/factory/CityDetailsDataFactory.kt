package gaston.gsanguinetti.glovo.workingarea.data.factory

import gaston.gsanguinetti.glovo.base.test.randomBoolean
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.data.model.CityDetails

class CityDetailsDataFactory {
    companion object {
        fun makeCityDetails(cityCode :String = randomString()) =
                CityDetails(
                    randomBoolean(),
                    cityCode,
                    randomString(),
                    randomString(),
                    randomBoolean(),
                    randomString(),
                    randomString(),
                    randomString()
                )
    }
}