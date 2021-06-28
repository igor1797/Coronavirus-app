package hr.dice.coronavirus.app.ui.maps

import android.content.Intent
import android.net.Uri
import androidx.navigation.Navigation
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.common.sharedGraphViewModel
import hr.dice.coronavirus.app.databinding.FragmentMapsBinding
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.model.one_country.CountryStatus
import hr.dice.coronavirus.app.ui.activity.MainActivity
import hr.dice.coronavirus.app.ui.base.BaseFragment
import hr.dice.coronavirus.app.ui.base.CountrySelected
import hr.dice.coronavirus.app.ui.base.WorldWide
import hr.dice.coronavirus.app.ui.base.onSuccess
import hr.dice.coronavirus.app.ui.home.fragments.presentation.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsFragment : BaseFragment<FragmentMapsBinding>() {

    private val homeViewModel by sharedGraphViewModel<HomeViewModel>(R.id.home_container_graph)
    private val mapsViewModel: MapsViewModel by viewModel()
    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
        getStatisticsMapsData()
    }

    override val layoutResourceId: Int get() = R.layout.fragment_maps

    override fun onPostViewCreated() {
        binding.viewModel = homeViewModel
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        homeViewModel.coronaDataStatus.observe(viewLifecycleOwner) {
            it.onSuccess<Any> {
                mapFragment?.getMapAsync(callback)
                initViewModelObservers()
            }
        }
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            error.tryAgain.setOnClickListener {
                tryAgainToFetchStatisticsData()
            }
            noInternetConnection.tryAgain.setOnClickListener {
                tryAgainToFetchStatisticsData()
            }
            emptyState.backToSearch.setOnClickListener {
                navigateToCountrySelection()
            }
        }
    }

    private fun tryAgainToFetchStatisticsData() {
        homeViewModel.getStatisticsData()
    }

    private fun navigateToCountrySelection() {
        val controller = Navigation.findNavController(activity as MainActivity, R.id.mainNavHostFragment)
        controller.navigate(R.id.goToCountrySelectionFragment)
    }

    private fun getStatisticsMapsData() {
        with(homeViewModel) {
            val response = coronaDataStatus.value
            useCase.value?.let {
                when (it) {
                    is CountrySelected -> {
                        handleCountrySelected()
                        response?.onSuccess<CountryStatus> { countryStatus ->
                            handleCountrySelectedSuccess(countryStatus)
                        }
                    }
                    WorldWide -> {
                        handleWorldWideSelected()
                        response?.onSuccess<GlobalStatus> { globalStatus ->
                            handleWorldWideSuccess(globalStatus)
                        }
                    }
                }
            }
        }
    }

    private fun initViewModelObservers() {
        mapsViewModel.countriesLatLng.observe(viewLifecycleOwner) {
            it?.let { countriesLatLng ->
                if (countriesLatLng.size > 1) {
                    countriesLatLng.forEach { countryLatLng ->
                        addGlobalCountryMarkerOnMap(
                            countryLatLng.name,
                            countryLatLng.lat,
                            countryLatLng.lng
                        )
                    }
                    moveCameraOnMapToCountryLatLongAndZoomOutWorld(
                        countriesLatLng.first().lat,
                        countriesLatLng.first().lng
                    )
                } else {
                    val country = countriesLatLng.firstOrNull()
                    country?.let {
                        moveCameraOnMapToCountryLatLng(country.lat, country.lng)
                        addCountryMarkerOnMap(country.lat, country.lng)
                    }
                }
            } ?: showErrorSnackbar()
        }
    }

    private fun handleCountrySelected() {
        binding.mapsHeader.text = getText(R.string.statisticsByCountryText)
        map.setOnMarkerClickListener { marker ->
            openGoogleMapsWithTurnByTurnLocation(marker.tag as LatLng)
            false
        }
    }

    private fun handleCountrySelectedSuccess(countryStatus: CountryStatus) {
        with(binding) {
            casesStatus = countryStatus.casesStatus
            selection.text = countryStatus.name
        }
        mapsViewModel.getCountriesFromLocationName(countryStatus.name)
    }

    private fun handleWorldWideSelected() {
        setHeaderTextToStatisticsByWordwide()
        map.setOnMarkerClickListener { marker ->
            setHeaderTextToStatisticsByCountry()
            mapsViewModel.changeUserSelection(marker.tag as String)
            false
        }
        map.setOnMapClickListener { latLng ->
            setHeaderTextToStatisticsByWordwide()
            mapsViewModel.findIfCountryIsInTopThreeCountries(latLng.latitude, latLng.longitude)
        }
        mapsViewModel.mapsStatisticsData.observe(viewLifecycleOwner) {
            binding.casesStatus = it.casesStatus
            binding.selection.text = it.name
        }
    }

    private fun setHeaderTextToStatisticsByCountry() {
        binding.mapsHeader.text = getText(R.string.statisticsByCountryText)
    }

    private fun setHeaderTextToStatisticsByWordwide() {
        binding.mapsHeader.text = getText(R.string.statisticsWorldwideText)
    }

    private fun handleWorldWideSuccess(globalStatus: GlobalStatus) {
        with(binding) {
            casesStatus = globalStatus.casesStatus
            selection.text = getText(R.string.worldwideText)
        }
        mapsViewModel.apply {
            setTopThreeCountriesByConfirmedStates(globalStatus.topThreeCountriesByConfirmedCases)
            setWorldwideGlobalStatus(globalStatus)
        }

        globalStatus.topThreeCountriesByConfirmedCases.forEach { globalCountry ->
            mapsViewModel.getCountriesFromLocationName(globalCountry.name)
        }
    }

    private fun addCountryMarkerOnMap(lat: Double, long: Double) {
        val country = LatLng(lat, long)
        val marker = map.addMarker(MarkerOptions().position(country))
        marker.tag = country
    }

    private fun addGlobalCountryMarkerOnMap(name: String, lat: Double, long: Double) {
        val country = LatLng(lat, long)
        val marker = map.addMarker(MarkerOptions().position(country))
        marker.tag = name
    }

    private fun moveCameraOnMapToCountryLatLng(lat: Double, long: Double) {
        val country = LatLng(lat, long)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(country, 1F))
    }

    private fun moveCameraOnMapToCountryLatLongAndZoomOutWorld(lat: Double, long: Double) {
        val country = LatLng(lat, long)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(country, 1F))
    }

    private fun showErrorSnackbar() {
        Snackbar.make(binding.root, getText(R.string.markerErrorMessage), Snackbar.LENGTH_SHORT).show()
    }

    private fun openGoogleMapsWithTurnByTurnLocation(country: LatLng) {
        val directionUri = Uri.parse("google.navigation:q=${country.latitude},${country.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, directionUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(mapIntent)
        }
    }
}
