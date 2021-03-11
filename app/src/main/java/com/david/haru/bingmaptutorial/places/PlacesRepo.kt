package com.david.haru.bingmaptutorial.places

import android.location.Location
import com.david.haru.bingmaptutorial.GOOGLE_PLACES_API_KEY
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PlacesRepo @Inject constructor(
        private val webService: IPlacesApiService
) {

    suspend fun getPlaces(location: Location): RepoResponse<Place.ApiResult> {

        return try {
            val result: Place.ApiResult = webService.getPlaces(key = GOOGLE_PLACES_API_KEY, location = "" + location.latitude + "," + location.longitude)
            RepoResponse(
                    data = result,
                    status = ResultType.SUCCESS
            )
        } catch (io: IOException) {
            RepoResponse(
                    status = ResultType.FAIL,
                    throwable = io
            )
        } catch (http: HttpException) {
            RepoResponse(
                    status = ResultType.FAIL,
                    throwable = http
            )
        }

    }

}