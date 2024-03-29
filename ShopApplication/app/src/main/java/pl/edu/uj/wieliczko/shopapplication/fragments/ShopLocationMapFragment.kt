package pl.edu.uj.wieliczko.shopapplication.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pl.edu.uj.wieliczko.shopapplication.R

class ShopLocationMapFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        val shop = LatLng(51.40547, 19.70321)
        googleMap.addMarker(MarkerOptions().position(shop).title("Shop Location").visible(true))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(shop))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_location_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}