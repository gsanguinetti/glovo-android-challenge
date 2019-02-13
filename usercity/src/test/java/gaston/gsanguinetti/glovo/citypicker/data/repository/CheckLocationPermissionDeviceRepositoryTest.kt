package gaston.gsanguinetti.glovo.citypicker.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CheckLocationPermissionDeviceRepositoryTest {

    private lateinit var checkLocationPermissionDeviceRepository: CheckLocationPermissionDeviceRepository

    private lateinit var context: Context
    private lateinit var permissionChecker: PermissionChecker

    @Before
    fun before() {
        context = mock()
        permissionChecker = mock()
        checkLocationPermissionDeviceRepository = CheckLocationPermissionDeviceRepository(context, permissionChecker)
    }

    @Test
    fun checkPermissionsReturnsData() {
        whenever(permissionChecker.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
            .thenReturn(PackageManager.PERMISSION_GRANTED)

        checkLocationPermissionDeviceRepository.hasLocationPermissionGranted()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(true)
    }

    @Test
    fun checkPermissionsDenied() {
        whenever(permissionChecker.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION))
            .thenReturn(PackageManager.PERMISSION_DENIED)

        checkLocationPermissionDeviceRepository.hasLocationPermissionGranted()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
            .assertValue(false)
    }
}