package gaston.gsanguinetti.glovo.citypicker.data.mapper

import gaston.gsanguinetti.glovo.citypicker.data.factory.CityPickerDataFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AvailableCityMapperTest {

    private lateinit var availableCityMapper: AvailableCityMapper

    @Before
    fun before() {
        availableCityMapper = AvailableCityMapper()
    }

    @Test
    fun mapFromCity() {
        val city = CityPickerDataFactory.makeCity()
        availableCityMapper.mapFromEntity(city).run {
            assertEquals(code, city.code)
            assertEquals(name, city.name)
            assertEquals(workingArea, city.workingArea)
            assertEquals(countryCode, city.countryCode)
        }
    }
}