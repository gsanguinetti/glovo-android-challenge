package gaston.gsanguinetti.glovo.base.test

import android.app.Application
import android.content.Context
import org.koin.dsl.module.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito

abstract class AbstractModuleTest : AutoCloseKoinTest() {

    protected val mockedContextModule = listOf (
        module {
            single { Mockito.mock(Context::class.java) }
            single { Mockito.mock(Application::class.java) }
        }
    )
}