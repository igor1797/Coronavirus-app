package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.country_selection.presentation.CountrySelectionViewModel
import hr.dice.coronavirus.app.ui.home.fragments.presentation.HomeViewModel
import hr.dice.coronavirus.app.ui.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { SplashViewModel() }

    viewModel { (initialUseCase: UseCase) -> HomeViewModel(get(), initialUseCase, get(), get()) }

    viewModel { CountrySelectionViewModel(get()) }
}
