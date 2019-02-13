package gaston.gsanguinetti.glovo.workingarea.data.factory

import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.base.test.randomStringList
import gaston.gsanguinetti.glovo.workingarea.data.model.CityArea

class CityAreaDataFactory {
    companion object {

        fun makeCityArea(cityCode :String = randomString()) =
                CityArea(
                    cityCode,
                    randomStringList(5)
                )
    }
}