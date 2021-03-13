package com.david.haru.bingmaptutorial.ui

import android.Manifest
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.david.haru.bingmaptutorial.databinding.ActivityMainBinding
import com.david.haru.bingmaptutorial.places.Place
import com.david.haru.bingmaptutorial.ui.viewmodel.LocationViewModel
import com.david.haru.bingmaptutorial.ui.viewmodel.PlacesViewModel
import com.david.haru.bingmaptutorial.util.showToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val DELTA_LOCATION: Int = 10
    }

    private lateinit var binding: ActivityMainBinding

    private val locationViewModel: LocationViewModel by viewModels()
    private val placesViewModel: PlacesViewModel by viewModels()

    private lateinit var lastLocation: Location

    private val mapHelper: MapViewHelper by lazy {
        MapViewHelper(binding.mapView, lifecycle)
    }

    private val observeLocation = Observer<Location?> { currentLocation ->
        currentLocation?.let {
            if (!this::lastLocation.isInitialized) {
                // first time
                lastLocation = currentLocation
                placesViewModel.fetchPlaces(location = currentLocation)
            }

            if (lastLocation.distanceTo(currentLocation) > DELTA_LOCATION) {
                // update Places when getting new Locations
                lastLocation = currentLocation
                placesViewModel.fetchPlaces(location = currentLocation)
            }
        }
    }

    private val observePlaces = Observer<Place.ApiResult?> { places ->
        places?.let { places ->
            // Getting new  Places and update mapHelepr
            mapHelper.setPlaces(lastLocation = lastLocation, places = places)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapHelper.onCreate(savedInstanceState = savedInstanceState)
        getLocationPermission()
        observeViewModels()

    }

    private fun observeViewModels() {
        locationViewModel.getLiveDataLocation().observe(this, observeLocation)

        placesViewModel.getPlacesLiveData().observe(this, observePlaces)

        placesViewModel.getErrLiveData().observe(this, Observer {
            showToast(msg = "Error at obtaining places")
        }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapHelper.onSaveInstanceState(outState = outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapHelper.onLowMemory()
    }

    private fun getLocationPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        mapHelper.startTrackingUserLocation()
                        locationViewModel.getLiveDataLocation()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                        locationViewModel.showRational()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        locationViewModel.explainNoPermissionConsequence()
                    }

                }).check()
    }


}