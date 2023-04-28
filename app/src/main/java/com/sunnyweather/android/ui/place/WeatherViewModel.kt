package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    val weatherLiveData = Transformations.switchMap(locationLiveData){location ->
        Repository.getWeather(location.lng, location.lat)
    }

    fun getWeather(lng : String, lat : String){
        locationLiveData.value = Location(lng, lat)
    }

    // 暂存上一个城市的信息
    var locationName = ""
    var locationLng = ""
    var locationLat = ""
}