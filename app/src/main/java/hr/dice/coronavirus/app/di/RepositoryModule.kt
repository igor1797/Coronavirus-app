package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.repositories.NewsRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory {
        CoronavirusRepository(get())
    }

    factory {
        CountryRepository(get(), get())
    }

    factory {
        NewsRepository(get())
    }
}
