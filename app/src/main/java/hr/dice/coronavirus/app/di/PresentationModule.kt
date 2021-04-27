package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.ui.country_selection.presentation.CountrySelectionViewModel
import hr.dice.coronavirus.app.ui.home.fragments.presentation.HomeViewModel
import hr.dice.coronavirus.app.ui.latest_news_list.presentation.LatestNewsViewModel
import hr.dice.coronavirus.app.ui.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { SplashViewModel() }

    viewModel { HomeViewModel(get(), get()) }

    viewModel { CountrySelectionViewModel(get()) }

    viewModel { LatestNewsViewModel(get(), get()) }
}
