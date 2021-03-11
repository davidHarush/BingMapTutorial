package com.david.haru.bingmaptutorial.ui.viewmodel

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.david.haru.bingmaptutorial.baseclasses.BaseViewModel
import com.david.haru.bingmaptutorial.places.Place
import com.david.haru.bingmaptutorial.places.PlacesRepo
import com.david.haru.bingmaptutorial.places.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PlacesViewModel @Inject constructor(
        @ApplicationContext private val appContext: Context,
        private val placesRepo: PlacesRepo
) : BaseViewModel() {


    private var placesLiveData: MutableLiveData<Place.ApiResult> = MutableLiveData()
    private var errLiveData: MutableLiveData<String> = MutableLiveData()


    fun getPlacesLiveData(): LiveData<Place.ApiResult> {
        return placesLiveData
    }

    fun getErrLiveData(): LiveData<String> {
        return errLiveData
    }

    fun fetchPlaces(location: Location) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                placesRepo.getPlaces(location).let { repoResponse ->
                    if (repoResponse.status == ResultType.SUCCESS) {
                        placesLiveData.postValue(repoResponse.data)
                    } else {
                        errLiveData.postValue(repoResponse.throwable.message)
                    }
                }
            }
        }

    }
}