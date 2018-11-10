package com.openclassrooms.realestatemanager.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.maps.model.Geometry
import com.google.maps.model.AddressComponent



/**
 * Created by Adrien Deguffroy on 07/11/2018.
 */
class GeocodeInfo {

    @SerializedName("results")
    @Expose
    var results: List<Result>? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}

class Result {

    @SerializedName("address_components")
    @Expose
    var addressComponents: List<AddressComponent>? = null
    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String? = null
    @SerializedName("geometry")
    @Expose
    var geometry: Geometry? = null
    @SerializedName("place_id")
    @Expose
    var placeId: String? = null
    @SerializedName("types")
    @Expose
    var types: List<String>? = null

}

class AddressComponent {

    @SerializedName("long_name")
    @Expose
    var longName: String? = null
    @SerializedName("short_name")
    @Expose
    var shortName: String? = null
    @SerializedName("types")
    @Expose
    var types: List<String>? = null

}

class Bounds {

    @SerializedName("northeast")
    @Expose
    var northeast: Northeast? = null
    @SerializedName("southwest")
    @Expose
    var southwest: Southwest? = null

}

class Geometry {

    @SerializedName("bounds")
    @Expose
    var bounds: Bounds? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("location_type")
    @Expose
    var locationType: String? = null
    @SerializedName("viewport")
    @Expose
    var viewport: Viewport? = null

}

class GeocodeLocation {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}

class Northeast {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}

class Northeast_ {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}

class Southwest {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}

class Southwest_ {

    @SerializedName("lat")
    @Expose
    var lat: Double? = null
    @SerializedName("lng")
    @Expose
    var lng: Double? = null

}

class Viewport {

    @SerializedName("northeast")
    @Expose
    var northeast: Northeast_? = null
    @SerializedName("southwest")
    @Expose
    var southwest: Southwest_? = null

}