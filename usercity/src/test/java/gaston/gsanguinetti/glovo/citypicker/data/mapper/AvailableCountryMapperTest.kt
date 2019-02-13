package gaston.gsanguinetti.glovo.citypicker.data.mapper

import gaston.gsanguinetti.glovo.citypicker.data.factory.CityPickerDataFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AvailableCountryMapperTest {

    private lateinit var availableCountryMapper :AvailableCountryMapper

    @Before
    fun before() {
        availableCountryMapper = AvailableCountryMapper()
    }

    @Test
    fun mapFromCountry() {
        val country = CityPickerDataFactory.makeCountry()
        availableCountryMapper.mapFromEntity(country).run {
            Assert.assertEquals(code, country.code)
            Assert.assertEquals(name, country.name)
        }
    }
}