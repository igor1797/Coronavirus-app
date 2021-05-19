package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.repositories.ResourceRepository
import org.koin.dsl.module

val repositoryModule = module {

    single {
        CoronavirusRepository(get())
    }

    single {
        CountryRepository(get(), get())
    }

    single {
        ResourceRepository(get())
    }
}
