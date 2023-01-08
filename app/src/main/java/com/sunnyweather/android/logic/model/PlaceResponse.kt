package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    @SerializedName("id")val id : String, val name: String,
    @SerializedName("formatted_address") val address: String,
    val location: Location,
    @SerializedName("place_id")val placeId : String
)

data class Location(val lng: String, val lat: String)