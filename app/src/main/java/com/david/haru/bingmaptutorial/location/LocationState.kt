package com.david.haru.bingmaptutorial.location

sealed class LocationState {

    object Idle: LocationState()
    object Available: LocationState()
    data class Unavailable(val errMsg: String? = null): LocationState()
}