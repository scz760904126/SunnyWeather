package com.sunnyweather.android.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel(){
    private val searchLiveData = MutableLiveData<String>()  // query

    val placeLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query : String){
        searchLiveData.value = query
    }

    val placeList = ArrayList<Place>()  // 对界面上的城市数据进行缓存

    fun savePlace(place : Place) = Repository.savePlace(place)

    fun getPlace() = Repository.getPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}