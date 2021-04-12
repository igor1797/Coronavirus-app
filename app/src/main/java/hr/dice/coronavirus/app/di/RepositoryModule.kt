package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.repositories.HomeRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory {
        HomeRepository(get())
    }
}
