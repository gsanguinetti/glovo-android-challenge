package gaston.gsanguinetti.glovo.challenge.module

import gaston.gsanguinetti.glovo.base.test.AbstractModuleTest
import gaston.gsanguinetti.glovo.challenge.appModules
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.standalone.StandAloneContext
import org.koin.test.checkModules

@RunWith(JUnit4::class)
class AppModuleTest : AbstractModuleTest() {

    @Before
    fun before() {
        StandAloneContext.startKoin(appModules + mockedContextModule)
    }

    @Test
    fun check() = checkModules(appModules + mockedContextModule)
}