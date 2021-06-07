package hr.dice.coronavirus.app.repositories

import android.location.Geocoder
import hr.dice.coronavirus.app.model.maps.CountryLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepository(
    private val geocoder: Geocoder
) {

    suspend fun getFromLocationName(name: String): CountryLatLng? {
        return withContext(Dispatchers.Default) {
            try {
                val addresses = geocoder.getFromLocationName(name, 1)
                CountryLatLng(name, addresses[0].latitude, addresses[0].longitude)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getFromLocation(lat: Double, lng: Double): String? {
        return withContext(Dispatchers.Default) {
            try {
                geocoder.getFromLocation(lat, lng, 1)[0].countryName
            } catch (e: Exception) {
                null
            }
        }
    }
}
