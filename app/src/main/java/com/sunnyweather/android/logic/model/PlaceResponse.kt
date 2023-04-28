package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: List<Place>) {
}

data class Place(
    val name: String,
    val location: Location,
    @SerializedName("formatted_address") val address: String    // JSON字段和Kotlin字段建立映射
)

data class Location(val lng: String, val lat: String)