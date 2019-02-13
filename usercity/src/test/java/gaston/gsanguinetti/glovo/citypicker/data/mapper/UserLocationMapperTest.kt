package gaston.gsanguinetti.glovo.citypicker.data.mapper

import android.location.Location
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomDouble
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserLocationMapperTest {
    private lateinit var userLocationMapper: UserLocationMapper

    @Before
    fun before() {
        userLocationMapper = UserLocationMapper()
    }

    @Test
    fun mapLocation() {
        val latitude = randomDouble()
        val longitude = randomDouble()

        val location: Location = mock()
        whenever(location.latitude).thenReturn(latitude)
        whenever(location.longitude).thenReturn(longitude)

        userLocationMapper.mapFromEntity(location).run {
            assertEquals(latitude, location.latitude)
            assertEquals(longitude, location.longitude)
        }
    }
}