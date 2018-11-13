package com.openclassrooms.realestatemanager.Controller.Fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.Models.GeocodeInfo
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.GeocodeStream
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import android.widget.Toast
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


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
                handleError(e)
            }

            override fun onComplete() {}
        }
    }

    protected fun handleError(throwable: Throwable) {
        activity?.runOnUiThread{
            when (throwable) {
                is HttpException -> {
                    val statusCode = throwable.code()
                    Log.e("HttpException", "Error code : $statusCode")
                    Toast.makeText(context, resources.getString(R.string.http_error_message, statusCode), Toast.LENGTH_SHORT).show()
                }
                is SocketTimeoutException -> {
                    Log.e("SocketTimeoutException", "Timeout from retrofit")
                    Toast.makeText(context, resources.getString(R.string.timeout_error_message), Toast.LENGTH_SHORT).show()
                }
                is IOException -> {
                    Log.e("IOException", "Error")
                    Toast.makeText(context, resources.getString(R.string.exception_error_message), Toast.LENGTH_SHORT).show()
                }
                is TimeoutException -> {
                    Log.e("TimeoutException", "Error")
                    Toast.makeText(context, resources.getString(R.string.timeout_error_message), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.e("Generic handleError", "Error : ${throwable.javaClass}")
                    Toast.makeText(context, resources.getString(R.string.generic_error_message), Toast.LENGTH_SHORT).show()
                }
            }
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

    fun getMarkerIconFromDrawable(): BitmapDescriptor? {
        val icon: Drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context!!.resources.getDrawable(R.drawable.baseline_location_on_black_36, context!!.applicationContext.theme)
        }else{
            context!!.resources.getDrawable(R.drawable.baseline_location_on_black_36)
        }
        val colorGreen = ContextCompat.getColor(context!!, R.color.colorAccent)
        val filter = LightingColorFilter(colorGreen, colorGreen)
        icon.colorFilter = filter

        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(icon.intrinsicWidth, icon.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        icon.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    // -------------------
    // ABSTRACT FUN
    // -------------------

    abstract fun getPositionFromRxRequest(position:LatLng?, estateId: Long)
}