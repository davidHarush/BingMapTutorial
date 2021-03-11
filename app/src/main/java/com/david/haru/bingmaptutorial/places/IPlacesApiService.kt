package com.david.haru.bingmaptutorial.places

import retrofit2.http.GET
import retrofit2.http.Query

interface IPlacesApiService {

    companion object {
        const val BASE_URL: String = "https://maps.googleapis.com/"
    }

    @GET("/maps/api/place/nearbysearch/json?radius=5500")
    suspend fun getPlaces(@Query("location") location: String, @Query("key") key: String): Place.ApiResult
}