package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.Models.GeocodeInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Adrien Deguffroy on 07/11/2018.
 */
class GeocodeStream {

    private var mGeocodeService = GeocodeService.create()

    fun streamFetchGeocodeInfo(address:String,key:String):Observable<GeocodeInfo> =
            GeocodeService.create().getGeocodeInfo(address, key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(10,TimeUnit.SECONDS)
}