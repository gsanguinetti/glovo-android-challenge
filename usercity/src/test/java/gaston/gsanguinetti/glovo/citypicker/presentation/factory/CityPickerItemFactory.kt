package gaston.gsanguinetti.glovo.citypicker.presentation.factory

import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityItem
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CityPickerItem
import gaston.gsanguinetti.glovo.citypicker.presentation.model.CountryItem

class CityPickerItemListFactory {
    companion object {
        fun makeCityPickerItemList(numItems: Int): List<CityPickerItem> =
            (0 until numItems).map {
                CityPickerItem(
                    makeCountryItem(),
                    (0 until numItems).map { makeCityItem() }
                )
            }

        fun makeCountryItem(): CountryItem = CountryItem(randomString())
        fun makeCityItem(): CityItem = CityItem(randomString(), randomString())
    }
}