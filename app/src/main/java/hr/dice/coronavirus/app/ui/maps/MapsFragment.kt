package hr.dice.coronavirus.app.ui.maps

import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.model.LatLng
import hr.dice.coronavirus.app.R
import hr.dice.coronavirus.app.databinding.FragmentMapsBinding
import hr.dice.coronavirus.app.ui.base.BaseFragment

class MapsFragment : BaseFragment<FragmentMapsBinding>() {

    private val callback = OnMapReadyCallback { googleMap ->
        val brazil = LatLng(-14.24, -51.93)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(brazil, 3F))
    }

    override val layoutResourceId: Int get() = R.layout.fragment_maps

    override fun onPostViewCreated() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}