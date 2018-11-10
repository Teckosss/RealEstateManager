package com.openclassrooms.realestatemanager.Controller.Fragments

import android.support.v4.app.Fragment
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.Models.GeocodeInfo
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.GeocodeStream
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by Adrien Deguffroy on 09/11/2018.
 */
abstract class BaseFragment : Fragment() {

    var disposable:Disposable? = null

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    fun executeHttpRequestWithRetrofit(address:String, estateId: Long){
        this.disposable = GeocodeStream().streamFetchGeocodeInfo(address, resources.getString(R.string.google_maps_api_key)).subscribeWith( createObserver(estateId))
    }

    private fun <T> createObserver(estateId: Long): DisposableObserver<T> {
        return object : DisposableObserver<T>() {
            override fun onNext(t: T) {
                if (t is GeocodeInfo){
                    retrieveAddressFromOnNext(t, estateId)
                }
            }

            override fun onError(e: Throwable) {
                TODO() //handleError(e)
            }

            override fun onComplete() {}
        }
    }

    fun disposeWhenDestroy(){
        if(this.disposable != null && !this.disposable?.isDisposed!!) this.disposable?.dispose()
    }

    // -------------------
    // ACTION
    // -------------------

    private fun retrieveAddressFromOnNext(result:GeocodeInfo, estateId: Long){
        var position : LatLng? = null
        val locationLat = result.results?.get(0)?.geometry?.location?.lat
        val locationLng = result.results?.get(0)?.geometry?.location?.lng
        if (locationLat != null && locationLng != null) position = LatLng(locationLat,locationLng)

        this.getPositionFromRxRequest(position,estateId)
    }

    fun getFullAddressFromEstate(location:com.openclassrooms.realestatemanager.Models.Location):String?
            = if (location.address != "" && location.city != "" && location.country != "" && location.zipCode != "") location.address + " " + location.city + ", " + location.country + " " + location.zipCode else null


    // -------------------
    // ABSTRACT FUN
    // -------------------

    abstract fun getPositionFromRxRequest(position:LatLng?, estateId: Long)
}