package gaston.gsanguinetti.glovo.base.module

import gaston.gsanguinetti.glovo.base.domain.util.PolygonUtil
import org.koin.dsl.module.module

val base = module {
    factory { PolygonUtil() }
}