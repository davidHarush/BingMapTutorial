package com.david.haru.bingmaptutorial.location


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

/**
 * Created by David Harush
 * On 09/03/2021.
 */

@Singleton
class LocationRepositoryImpl @Inject constructor(
        @ApplicationContext private val appContext: Context,
        private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    override val locationStateLiveData: LiveData<LocationState> = MutableLiveData(LocationState.Idle)
    override val locationLiveData: LiveData<Location?> = MutableLiveData(null)

    init {
        GlobalScope.launch {
            locationLiveData as MutableLiveData
            locationLiveData.postValue(getLastKnownLocation())
        }
    }

    override suspend fun getLastKnownLocation(): Location? = suspendCoroutine { continuation ->
        val permittedState = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permittedState != PackageManager.PERMISSION_GRANTED) {
            continuation.resumeWith(Result.success(null))
        } else {
            fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                continuation.resumeWith(Result.success(task.result))
            }
        }
    }

    override fun registerLocationRequestIfPossible(locationRequest: LocationRequest) {
        val permissionState = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.removeLocationUpdates(locationUpdatesCallback)
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationUpdatesCallback,
                    Looper.getMainLooper()
            )
        }
    }

    override fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationUpdatesCallback)
        locationStateLiveData as MutableLiveData
        locationStateLiveData.value = LocationState.Idle
    }

    private val locationUpdatesCallback = object : LocationCallback() {

        override fun onLocationAvailability(p0: LocationAvailability?) {
            val availability = p0 ?: return

            locationStateLiveData as MutableLiveData
            locationStateLiveData.postValue(if (availability.isLocationAvailable)
                LocationState.Available
            else
                LocationState.Unavailable()
            )
        }

        override fun onLocationResult(p0: LocationResult?) {
            val locationResult = p0 ?: return
            locationLiveData as MutableLiveData
            locationLiveData.postValue(locationResult.lastLocation)

        }
    }
}