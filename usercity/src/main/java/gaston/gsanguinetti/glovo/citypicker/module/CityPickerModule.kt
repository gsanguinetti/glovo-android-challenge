package gaston.gsanguinetti.glovo.citypicker.module

import com.google.android.gms.location.LocationServices
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import gaston.gsanguinetti.glovo.citypicker.data.mapper.AvailableCityMapper
import gaston.gsanguinetti.glovo.citypicker.data.mapper.AvailableCountryMapper
import gaston.gsanguinetti.glovo.citypicker.data.mapper.UserLocationMapper
import gaston.gsanguinetti.glovo.citypicker.data.repository.*
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCitiesRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.AvailableCountriesRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.CheckLocationPermissionRepository
import gaston.gsanguinetti.glovo.citypicker.domain.repository.UserLocationRepository
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.CheckLocationPermissionUseCase
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.FetchUserCityCodeUseCase
import gaston.gsanguinetti.glovo.citypicker.domain.usecase.LocationsToPickUseCase
import gaston.gsanguinetti.glovo.citypicker.presentation.mapper.CityPickerItemMapper
import gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel.CityPickerViewModel
import gaston.gsanguinetti.glovo.citypicker.presentation.viewmodel.UserLocationFetchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val cityPicker = module {
    factory { CheckLocationPermissionUseCase(get()) }
    factory { CheckLocationPermissionDeviceRepository(androidContext(), get()) as CheckLocationPermissionRepository }
    factory { PermissionChecker() }
    factory { LocationServices.getFusedLocationProviderClient(androidContext()) }
    factory { UserLocationMapper() }
    factory { UserLocationDeviceRepository(get(), get()) as UserLocationRepository }
    factory { AvailableCitiesNetworkRepository(get(), get()) as AvailableCitiesRepository }
    factory { AvailableCountriesNetworkRepository(get(), get()) as AvailableCountriesRepository }
    factory { AvailableCityMapper() }
    factory { AvailableCountryMapper() }
    factory { FetchUserCityCodeUseCase(get(), get(), get()) }
    factory { LocationsToPickUseCase(get(), get()) }
    factory { CityPickerItemMapper() }
    factory { GroupAdapter<ViewHolder>() }
    viewModel { UserLocationFetchViewModel(get(), get()) }
    viewModel { CityPickerViewModel(get(), get()) }
}