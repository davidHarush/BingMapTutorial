package com.david.haru.bingmaptutorial.ui

import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.david.haru.bingmaptutorial.BING_MAP_CREDENTIALS_KEY
import com.david.haru.bingmaptutorial.places.Place
import com.david.haru.bingmaptutorial.util.PlacesDistanceComparator
import com.david.haru.bingmaptutorial.util.getAppContext
import com.david.haru.bingmaptutorial.util.isDarkThemeOn
import com.microsoft.maps.*
import java.util.*
import java.util.concurrent.TimeUnit


class MapViewHelper(mapViewContainer: ViewGroup, lifecycle: Lifecycle) : LifecycleObserver {
    private var mMapView: MapView = MapView(getAppContext(), MapRenderMode.VECTOR)

    private val locationProvider: GPSMapLocationProvider by lazy {
        GPSMapLocationProvider.Builder(getAppContext())
                .setDesiredProviders(ArrayList(listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER, LocationManager.PASSIVE_PROVIDER)))
                .setMinTime(TimeUnit.SECONDS.toMillis(0))
                .useLastKnownLocationOnLaunch()
                .build()
    }

    init {
        lifecycle.addObserver(this)
        mMapView.setCredentialsKey(BING_MAP_CREDENTIALS_KEY)
        mMapView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        mMapView.textDirection = View.TEXT_DIRECTION_RTL
        mapViewContainer.addView(mMapView)
    }


    fun startTrackingUserLocation() {
        mMapView.userLocation.apply {
            startTracking(locationProvider)
            trackingMode = MapUserLocationTrackingMode.CENTERED_ON_USER
        }
    }

    fun setPlaces(lastLocation: Location, places: Place.ApiResult) {
        mMapView.layers.clear()

        Collections.sort(places.results, PlacesDistanceComparator(lastLocation))

        val mPinLayer = MapElementLayer()
        places.results.subList(0, 5.coerceAtMost(5)).forEach { item ->
            val pushpin = MapIcon().apply {
                location = Geopoint(item.location)
                title = item.name
                isFlat = true
            }
            mPinLayer.elements.add(pushpin)
        }
        mMapView.layers.add(mPinLayer)
    }

    fun onLowMemory() {
        mMapView.onLowMemory()
    }

    fun onSaveInstanceState(outState: Bundle) {
        mMapView.onSaveInstanceState(outState)
    }

    fun onCreate(savedInstanceState: Bundle?) {
        mMapView.onCreate(savedInstanceState)
        initStyle()
    }


    /*************************************
     *  LIFECYCLE EVENTS
     ************************************/

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mMapView.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mMapView.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mMapView.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mMapView.onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mMapView.onStop()
    }

    /*************************************
     * PRIVATE
     ************************************/

    private fun initStyle() {
        mMapView.mapStyleSheet = if (isDarkThemeOn()) {
            MapStyleSheets.vibrantDark()
        } else {
            MapStyleSheets.vibrantLight()
        }
    }

}