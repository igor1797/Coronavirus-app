package hr.dice.coronavirus.app.ui.splash.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _startHomeContainerScreen = MutableLiveData(false)
    val startHomeContainerScreen: LiveData<Boolean> get() = _startHomeContainerScreen

    init {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DELAY_TIME)
            _startHomeContainerScreen.value = true
        }
    }

    companion object {
        private const val SPLASH_SCREEN_DELAY_TIME = 2000L
    }
}
