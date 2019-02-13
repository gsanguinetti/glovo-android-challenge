package gaston.gsanguinetti.glovo.citypicker.domain.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import gaston.gsanguinetti.glovo.base.test.randomBoolean
import gaston.gsanguinetti.glovo.citypicker.domain.repository.CheckLocationPermissionRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CheckLocationPermissionUseCaseTest {

    private lateinit var checkLocationPermissionRepository: CheckLocationPermissionRepository

    private lateinit var checkLocationPermissionUseCase: CheckLocationPermissionUseCase

    @Before
    fun before() {
        checkLocationPermissionRepository = mock()
        checkLocationPermissionUseCase = CheckLocationPermissionUseCase(checkLocationPermissionRepository)
    }

    @Test
    fun buildUseCaseObservableCallsRepository() {
        val permissionGranted = randomBoolean()
        stubLocationPermissionResponse(permissionGranted)

        checkLocationPermissionUseCase.buildUseCaseObservable()
            .blockingGet()
        verify(checkLocationPermissionRepository).hasLocationPermissionGranted()
    }

    @Test
    fun buildUseCaseObservableCallsRepositoryCompletes() {
        val permissionGranted = randomBoolean()
        stubLocationPermissionResponse(permissionGranted)

        checkLocationPermissionUseCase.buildUseCaseObservable()
            .test()
            .await()
            .assertNoErrors()
            .assertComplete()
    }

    private fun stubLocationPermissionResponse(permissionGranted :Boolean) {
        whenever(checkLocationPermissionRepository.hasLocationPermissionGranted())
            .thenReturn(Single.just(permissionGranted))
    }
}