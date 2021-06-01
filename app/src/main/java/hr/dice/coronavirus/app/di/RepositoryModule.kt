package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.repositories.CoronavirusRepository
import hr.dice.coronavirus.app.repositories.CountryRepository
import hr.dice.coronavirus.app.repositories.LocationRepository
import hr.dice.coronavirus.app.repositories.NewsRepository
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
        NewsRepository(get())
    }

    single {
        ResourceRepository(get())
    }

    single {
        LocationRepository(get())
    }
}
