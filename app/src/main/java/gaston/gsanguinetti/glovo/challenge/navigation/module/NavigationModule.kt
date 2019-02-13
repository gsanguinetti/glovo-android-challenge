package gaston.gsanguinetti.glovo.challenge.navigation.module

import gaston.gsanguinetti.glovo.challenge.navigation.presentation.NavigationViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val navigation = module {
    viewModel { NavigationViewModel() }
}