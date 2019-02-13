package gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import gaston.gsanguinetti.glovo.base.test.randomString
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.CheckLocationPermissionUseCase
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.FetchUserCityCodeUseCase
import io.reactivex.observers.DisposableSingleObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserLocationFetchViewModelTest {

    private lateinit var userLocationFetchViewModel: UserLocationFetchViewModel

    private lateinit var checkLocationPermissionUseCase: CheckLocationPermissionUseCase
    private lateinit var fetchUserCityCodeUseCase: FetchUserCityCodeUseCase

    private lateinit var fetchUserLocationPermissionExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<Boolean>>
    private lateinit var fetchUserLocationExecutorCaptor: KArgumentCaptor<DisposableSingleObserver<String>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        checkLocationPermissionUseCase = mock()
        fetchUserCityCodeUseCase = mock()

        userLocationFetchViewModel =
            UserLocationFetchViewModel(checkLocationPermissionUseCase, fetchUserCityCodeUseCase)

        fetchUserLocationExecutorCaptor = argumentCaptor()
        fetchUserLocationPermissionExecutorCaptor = argumentCaptor()
    }

    @Test
    fun callViewModelFetchLocationPermissionExecuteUseCase() {
        userLocationFetchViewModel.startUserLocationFetch()
        verify(checkLocationPermissionUseCase).execute(fetchUserLocationPermissionExecutorCaptor.capture(),
            eq(null))
    }

    @Test
    fun callViewModelPermissionGrantedFetchLocationExecuteUseCase() {
        userLocationFetchViewModel.onLocationPermissionGranted()
        verify(fetchUserCityCodeUseCase).execute(fetchUserLocationExecutorCaptor.capture(),
            eq(null))
    }

    @Test
    fun callViewModelPermissionDeniedCallForFinish() {
        val permissionDeniedObserver : Observer<Unit> = mock()
        userLocationFetchViewModel.locationPermissionError.observeForever(permissionDeniedObserver)
        userLocationFetchViewModel.onLocationPermissionDenied()
        verify(permissionDeniedObserver).onChanged(null)
    }

    @Test
    fun callViewModelFetchLocationLoadsData() {
        val cityCode = randomString()
        userLocationFetchViewModel.onLocationPermissionGranted()
        verify(fetchUserCityCodeUseCase).execute(fetchUserLocationExecutorCaptor.capture(), eq(null))
        fetchUserLocationExecutorCaptor.firstValue.onSuccess(cityCode)
        assertEquals(userLocationFetchViewModel.userCityCode.value, cityCode)
    }

    @Test
    fun callViewModelFetchLocationErrorEmptyCode() {
        userLocationFetchViewModel.onLocationPermissionGranted()
        verify(fetchUserCityCodeUseCase).execute(fetchUserLocationExecutorCaptor.capture(), eq(null))
        fetchUserLocationExecutorCaptor.firstValue.onError(Exception())
        assert(userLocationFetchViewModel.userCityCode.value!!.isEmpty())
    }
}