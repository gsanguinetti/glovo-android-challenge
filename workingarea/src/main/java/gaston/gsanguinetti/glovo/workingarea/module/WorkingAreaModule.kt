package gaston.gsanguinetti.glovo.workingarea.module

import gaston.gsanguinetti.glovo.workingarea.data.mapper.CityAreaMapper
import gaston.gsanguinetti.glovo.workingarea.data.mapper.CityDetailsMapper
import gaston.gsanguinetti.glovo.workingarea.data.repository.CityDetailsNetworkRepository
import gaston.gsanguinetti.glovo.workingarea.data.repository.WorkingAreaCitiesNetworkRepository
import gaston.gsanguinetti.glovo.workingarea.domain.repository.CityDetailsRepository
import gaston.gsanguinetti.glovo.workingarea.domain.repository.WorkingAreaCitiesRepository
import gaston.gsanguinetti.glovo.workingarea.domain.usecase.CityDetailsUseCase
import gaston.gsanguinetti.glovo.workingarea.domain.usecase.MapContentUseCase
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.CityDetailsContentMapper
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.GeometryNormalizer
import gaston.gsanguinetti.glovo.workingarea.presentation.mapper.MapPresentationContentMapper
import gaston.gsanguinetti.glovo.workingarea.presentation.viewmodel.WorkingAreaViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val workingArea = module {

    // Presentation layer injections
    viewModel { WorkingAreaViewModel(get(), get(), get(), get()) }
    factory { CityDetailsContentMapper() }
    factory { GeometryNormalizer() }
    factory { MapPresentationContentMapper(get()) }

    // Domain layer injections
    factory { CityDetailsUseCase(get(), get(), get()) }
    factory { MapContentUseCase(get(), get()) }

    // Data layer injections
    factory { CityAreaMapper() }
    factory { CityDetailsMapper() }
    factory { CityDetailsNetworkRepository(get(), get()) as CityDetailsRepository }
    factory { WorkingAreaCitiesNetworkRepository(get(), get()) as WorkingAreaCitiesRepository }
}