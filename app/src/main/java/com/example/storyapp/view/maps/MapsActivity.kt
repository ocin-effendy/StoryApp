package com.example.storyapp.view.maps

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.MainViewModel
import com.example.storyapp.R
import com.example.storyapp.data.remote.Result
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mainViewModel = viewModels<MainViewModel> {
            ViewModelFactory.getInstance()
        }.value

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val tokenId = intent.getStringExtra(TOKEN)
        mainViewModel.getSearchDataUser(null, null, 1, tokenId!!)

        mainViewModel.listStory.observe(this){ result ->
            when (result) {
                is Result.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    val data = result.data.listStory as List<ListStoryItem>
                    val indonesiaBounds = LatLngBounds(LatLng(-11.0, 95.0), LatLng(6.0, 141.0))
                    val latLngBuilder = LatLngBounds.Builder()
                    data.forEach { position ->
                        val lat = position.lat as Double
                        val lon = position.lon as Double
                        val latLng = LatLng(lat, lon)
                        val addressName = getAddressName(lat, lon)
                        if (indonesiaBounds.contains(latLng)) {
                            mMap.addMarker(
                                MarkerOptions().position(latLng).title(position.name).snippet(addressName)
                            )
                            latLngBuilder.include(latLng)
                        }
                    }
                    val indonesiaBoundsCenter = latLngBuilder.build().center

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(indonesiaBoundsCenter))
                }
                is Result.Error -> {
                    Toast.makeText(this, "Data Failed", Toast.LENGTH_SHORT).show()

                }
            }
        }
        setMapStyle()

    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d(TAG, "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }


    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }


    companion object{
        const val TOKEN = "TOKEN"
    }
}