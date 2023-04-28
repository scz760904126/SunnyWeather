package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if ("ok" == placeResponse.status) {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun getWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async { SunnyWeatherNetwork.getRealtimeWeather(lng, lat) }
            val deferredDaily = async { SunnyWeatherNetwork.getDailyWeather(lng, lat) }
            val realtimeResponse =
                deferredRealtime.await() // 使用async函数并发的发送两个请求，需要额外使用coroutineScope创建协程作用域
            val dailyResponse = deferredDaily.await()
            if ("ok" == realtimeResponse.status && "ok" == dailyResponse.status) {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response is ${realtimeResponse.status} +" +
                                " daily response is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    /**
     * 统一进行try-catch处理，为了保证Lambda表达式中的代码一定也是在挂起函数中，需要增加suspend关键字
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    /**
    * 对Dao层的接口进行封装，后续这里可以进行优化，额外开启线程进行文件读取，并返回LiveData
    */
    fun savePlace(place : Place) = PlaceDao.savePlace(place)

    fun getPlace() = PlaceDao.getPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}


