package gaston.gsanguinetti.glovo.workingarea.data.mapper

import gaston.gsanguinetti.glovo.workingarea.data.factory.CityAreaDataFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CityAreaMapperTest {

    private lateinit var cityAreaMapper: CityAreaMapper

    @Before
    fun before() {
        cityAreaMapper = CityAreaMapper()
    }

    @Test
    fun mapToAvailableCity() {
        val cityArea = CityAreaDataFactory.makeCityArea()
        cityAreaMapper.mapFromEntity(cityArea).run {
            assertEquals(cityArea.code, code)
            assertEquals(cityArea.workingArea, workingArea)
        }
    }
}