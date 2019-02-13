package gaston.gsanguinetti.glovo.challenge.module

import gaston.gsanguinetti.glovo.challenge.BuildConfig
import gaston.gsanguinetti.glovo.network.datasource.model.ServerAddress
import org.koin.dsl.module.module

val app = module {
    factory { ServerAddress(BuildConfig.SERVER_BASE_URL) }
}