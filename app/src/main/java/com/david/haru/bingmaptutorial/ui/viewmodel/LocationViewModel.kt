package com.david.haru.bingmaptutorial.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.david.haru.bingmaptutorial.baseclasses.BaseViewModel
import com.david.haru.bingmaptutorial.location.LocationRepository
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by David Harush
 * On 09/03/2021.
 */
@HiltViewModel
class LocationViewModel @Inject constructor(
        @ApplicationContext private val appContext: Context,
        private val locationRepository: LocationRepository
) : BaseViewModel() {

    var locationLiveData: LiveData<Location?> = locationRepository.locationLiveData


    fun getLiveDataLocation(): LiveData<Location?> {
        val permissionState =
                ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = TimeUnit.SECONDS.toMillis(10)
                fastestInterval = TimeUnit.SECONDS.toMillis(3)
            }
            locationRepository.registerLocationRequestIfPossible(locationRequest)
        }

        return locationLiveData
    }


    fun showRational() {
    }

    fun explainNoPermissionConsequence() {
    }


}