package gaston.gsanguinetti.glovo.citypicker.presentation.mapper

import gaston.gsanguinetti.glovo.base.test.randomInt
import gaston.gsanguinetti.glovo.citypicker.domain.factory.LocationOptionsFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityItemPickerMapperTest {

    private lateinit var cityPickerItemMapper: CityPickerItemMapper

    @Before
    fun before() {
        cityPickerItemMapper = CityPickerItemMapper()
    }

    @Test
    fun mapFromAvailableCountry() {
        val numCities = randomInt()
        val availableCountry = LocationOptionsFactory.makeAvailableCountry(numCities)

        val cityPickerItem = cityPickerItemMapper.mapToEntity(availableCountry)

        assertEquals(cityPickerItem.countryItem.name, availableCountry.name)
        (0 until numCities).forEach {
            assertEquals(cityPickerItem.cityItems[it].code, availableCountry.cities[it].code)
            assertEquals(cityPickerItem.cityItems[it].label, availableCountry.cities[it].name)
        }
    }
}