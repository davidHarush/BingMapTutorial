package com.david.haru.bingmaptutorial.places

import android.location.Location

object Place {

    data class ApiResult(
            val results: ArrayList<Item>,
            val status: String
    )

    data class Item(
            val name: String = "",
            val geometry: Geometry
    ) {
        val location: Location
            get() {
                val loc = Location("")
                loc.latitude = geometry.location.lat
                loc.longitude = geometry.location.lng
                return loc
            }
    }

    data class Geometry(
            val location: Loc
    )

    data class Loc(
            val lat: Double = 0.0,
            val lng: Double = 0.0,
    )
}