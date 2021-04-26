package hr.dice.coronavirus

import android.app.Application
import hr.dice.coronavirus.app.di.appModule
import hr.dice.coronavirus.app.di.presentationModule
import hr.dice.coronavirus.app.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CoronavirusApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoronavirusApp)
            modules(
                listOf(
                    appModule,
                    presentationModule,
                    repositoryModule
                )
            )
        }
    }
}
