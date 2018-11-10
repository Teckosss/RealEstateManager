package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.Models.GeocodeInfo
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Adrien Deguffroy on 07/11/2018.
 */
interface GeocodeService {
    @GET("json?sensor=false")
    fun getGeocodeInfo(@Query("address") address: String, @Query("key") key: String): Observable<GeocodeInfo>

    companion object {
        fun create():GeocodeService{
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.google.com/maps/api/geocode/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                    .build()

            return retrofit.create(GeocodeService::class.java)
        }
    }
}