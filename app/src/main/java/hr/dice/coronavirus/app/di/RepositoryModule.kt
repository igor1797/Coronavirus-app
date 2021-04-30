package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.repositories.CountryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single {
        CoronavirusRepository(get())
    }

    factory {
        CountryRepository(get(), get())
    }
}
