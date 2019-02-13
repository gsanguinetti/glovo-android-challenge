package gaston.gsanguinetti.glovo.workingarea.data.mapper

import gaston.gsanguinetti.glovo.workingarea.data.factory.CityDetailsDataFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityDetailsMapperTest {

    private lateinit var cityDetailsMapper: CityDetailsMapper

    @Before
    fun before() {
        cityDetailsMapper = CityDetailsMapper()
    }

    @Test
    fun mapToAvailableCityDetails() {
        val cityDetails = CityDetailsDataFactory.makeCityDetails()
        cityDetailsMapper.mapFromEntity(cityDetails).run {
            assertEquals(cityDetails.code, code)
            assertEquals(cityDetails.busy, busy)
            assertEquals(cityDetails.countryCode, countryCode)
            assertEquals(cityDetails.currency, currency)
            assertEquals(cityDetails.enabled, enabled)
            assertEquals(cityDetails.languageCode, languageCode)
            assertEquals(cityDetails.name, name)
            assertEquals(cityDetails.timeZone, timeZone)
        }
    }
}