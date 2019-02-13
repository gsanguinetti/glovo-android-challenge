package gaston.gsanguinetti.glovo.workingarea.presentation.mapper

import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.workingarea.domain.factory.AvailableCityDetailsDataFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityDetailsContentMapperTest {

    private lateinit var cityDetailsContentMapper: CityDetailsContentMapper

    @Before
    fun before() {
        cityDetailsContentMapper = CityDetailsContentMapper()
    }

    @Test
    fun mapFromAvailableCityDetails() {
        val availableCityDetails = AvailableCityDetailsDataFactory.makeAvailableCityDetails(randomString())
        cityDetailsContentMapper.mapToEntity(availableCityDetails).run {
            assertEquals(currency, availableCityDetails.currency)
            assertEquals(enabled, availableCityDetails.enabled)
            assertEquals(languageCode, availableCityDetails.languageCode)
            assertEquals(name, availableCityDetails.name)
            assertEquals(timeZone, availableCityDetails.timeZone)
        }
    }
}