package gaston.gsanguinetti.glovo.workingarea.domain.factory

import gaston.gsanguinetti.glovo.base.test.randomBoolean
import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.base.test.randomStringList
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCity
import gaston.gsanguinetti.glovo.workingarea.domain.model.AvailableCityDetails

class AvailableCityDetailsDataFactory {
    companion object {
        fun makeAvailableCityDetails(cityCode: String = randomString()) =
            AvailableCityDetails(
                randomBoolean(),
                cityCode,
                randomString(),
                randomString(),
                randomBoolean(),
                randomString(),
                randomString(),
                randomString()
            )

        fun makeAvailableCities(cityCode: String = randomString()) =
            mutableListOf<AvailableCity>()
                .apply { repeat(randomInt(1, 4)) { add(AvailableCityDetailsDataFactory.makeAvailableCity()) } }
                .apply { add(AvailableCityDetailsDataFactory.makeAvailableCity(cityCode)) }
                .apply { shuffle() }

        fun makeAvailableCity(cityCode: String = randomString()) =
            AvailableCity(
                cityCode,
                randomStringList(randomInt(1, 10))
            )
    }
}