package gaston.gsanguinetti.glovo.citypicker.data.repository

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.nhaarman.mockito_kotlin.*
import gaston.gsanguinetti.glovo.citypicker.data.mapper.UserLocationMapper
import gaston.gsanguinetti.glovo.citypicker.domain.factory.UserLocationFactory
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserLocationDeviceRepositoryTest {

    private lateinit var userLocationDeviceRepository: UserLocationDeviceRepository

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userLocationMapper: UserLocationMapper

    private lateinit var fetchUserLocationTaskCaptor: KArgumentCaptor<OnCompleteListener<Location>>

    @Before
    fun before() {
        fusedLocationClient = mock()
        userLocationMapper = mock()

        fetchUserLocationTaskCaptor = argumentCaptor()

        userLocationDeviceRepository = UserLocationDeviceRepository(fusedLocationClient, userLocationMapper)
    }

    @Test
    fun getUserLocationReturnsData() {
        val lastLocationTask: Task<Location> = mock()
        val location: Location = mock()
        val userLocation = UserLocationFactory.makeUserLocation()

        whenever(lastLocationTask.isSuccessful).thenReturn(true)
        whenever(lastLocationTask.result).thenReturn(location)
        whenever(userLocationMapper.mapFromEntity(location)).thenReturn(userLocation)

        whenever(fusedLocationClient.lastLocation).thenReturn(lastLocationTask)
        val userLocationSingle = userLocationDeviceRepository.fetchUserLocation().test()

        verify(lastLocationTask).addOnCompleteListener(fetchUserLocationTaskCaptor.capture())
        fetchUserLocationTaskCaptor.firstValue.onComplete(lastLocationTask)

        userLocationSingle
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(userLocation)
    }

    @Test
    fun getUserLocationMissing() {
        val lastLocationTask: Task<Location> = mock()
        val location: Location = mock()
        val userLocation = UserLocationFactory.makeUserLocation()

        whenever(lastLocationTask.isSuccessful).thenReturn(false)
        whenever(userLocationMapper.mapFromEntity(location)).thenReturn(userLocation)

        whenever(fusedLocationClient.lastLocation).thenReturn(lastLocationTask)
        val userLocationSingle = userLocationDeviceRepository.fetchUserLocation().test()

        verify(lastLocationTask).addOnCompleteListener(fetchUserLocationTaskCaptor.capture())
        fetchUserLocationTaskCaptor.firstValue.onComplete(lastLocationTask)

        userLocationSingle
            .await()
            .assertNotComplete()
    }
}