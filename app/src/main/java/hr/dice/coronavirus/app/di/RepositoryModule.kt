package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import org.koin.dsl.module

val repositoryModule = module {

    single {
        CoronavirusRepository(get())
    }
}
