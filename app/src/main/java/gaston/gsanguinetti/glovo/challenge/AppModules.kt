package gaston.gsanguinetti.glovo.challenge

import gaston.gsanguinetti.glovo.base.module.base
import gaston.gsanguinetti.glovo.challenge.module.app
import gaston.gsanguinetti.glovo.challenge.navigation.module.navigation
import gaston.gsanguinetti.glovo.citypicker.module.cityPicker
import gaston.gsanguinetti.glovo.network.datasource.module.networkDataSource
import gaston.gsanguinetti.glovo.workingarea.module.workingArea

val appModules =
    listOf(
        base,
        app,
        networkDataSource,
        navigation,
        cityPicker,
        workingArea
    )