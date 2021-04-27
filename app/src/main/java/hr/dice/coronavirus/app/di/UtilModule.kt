package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.common.utils.DateTimeUtil
import org.koin.dsl.module

val utilModule = module {
    factory {
        DateTimeUtil(get(), get())
    }
}
