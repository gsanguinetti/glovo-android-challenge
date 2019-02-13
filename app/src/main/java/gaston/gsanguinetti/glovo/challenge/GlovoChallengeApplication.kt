package gaston.gsanguinetti.glovo.challenge

import android.app.Application
import org.koin.android.ext.android.startKoin

class GlovoChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}