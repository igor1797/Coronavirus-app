package hr.dice.coronavirus.app.ui.home.fragments.container.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeContainerViewModel : ViewModel() {

    private val _mapsItemEnabled = MutableLiveData(false)
    val mapsItemEnabled: LiveData<Boolean> get() = _mapsItemEnabled

    private val _bottomNavVisible = MutableLiveData(true)
    val bottomNavVisible: LiveData<Boolean> get() = _bottomNavVisible

    fun disableMapsItem() {
        _mapsItemEnabled.value = false
    }

    fun enableMapsItem() {
        _mapsItemEnabled.value = true
    }

    fun setBottomNavVisible() {
        _bottomNavVisible.value = true
    }

    fun setBottomNavInvisible() {
        _bottomNavVisible.value = false
    }
}
