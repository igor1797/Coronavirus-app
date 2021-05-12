package hr.dice.coronavirus.app.di

import hr.dice.coronavirus.app.ui.base.UseCase
import hr.dice.coronavirus.app.ui.home.presentation.HomeViewModel
import hr.dice.coronavirus.app.ui.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { SplashViewModel() }

    viewModel { (initialUseCase: UseCase) -> HomeViewModel(get(), initialUseCase) }
}
