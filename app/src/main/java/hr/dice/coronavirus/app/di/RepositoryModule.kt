package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.common.utils.connectivity.Connectivity
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory { Connectivity(androidContext()) }
}
