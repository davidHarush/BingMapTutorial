package com.david.haru.bingmaptutorial.location

import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationRequest

/**
 * Created by David Harush
 * On 09/03/2021.
 */
interface LocationRepository {

    val locationLiveData: LiveData<Location?>

    val locationStateLiveData: LiveData<LocationState>

    suspend fun getLastKnownLocation(): Location?

    /**
     * Stops the last [LocationRequest] and starts a new one
     */
    fun registerLocationRequestIfPossible(locationRequest: LocationRequest)

    fun stopLocationUpdates()
}
