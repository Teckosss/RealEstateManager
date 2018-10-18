package com.openclassrooms.realestatemanager.Controller.ViewModel

import android.arch.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Adrien Deguffroy on 13/10/2018.
 */
class EstateViewModel(private val estateDataRepository: EstateDataRepository,
                      private val imageDataRepository: ImageDataRepository,
                      private val locationDataRepository: LocationDataRepository,
                      private val subscriberOn: Scheduler,
                      private val observerOn:Scheduler) : ViewModel() {

    private val disposable = CompositeDisposable()

    var lastIdInserted : MutableLiveData<Long> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        this.disposable.dispose()
    }

    fun updateLastIdInserted(newId:Long){
        lastIdInserted.value = newId
    }

    fun getLastIdInserted():Long{
        return lastIdInserted.value!!
    }

    // --------------------
    // ESTATE
    // --------------------

    fun getEstates(): LiveData<List<Estate>> {
        return estateDataRepository.getEstates()
    }

    fun createEstate(estate:Estate) {
        this.disposable.add(estateDataRepository.createEstate(estate)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {id -> updateLastIdInserted(id); Log.e("CREATE_ESTATE","OnNext")},
                        {e -> Log.e("CREATE_ESTATE","OnError : ${e.localizedMessage}")}
                )
        )
    }

    // --------------------
    // IMAGE
    // --------------------

    fun getImages(estateId:Long): LiveData<List<Image>>{
        return imageDataRepository.getImages(estateId)
    }

    fun createImage(image: Image) {
        this.disposable.add(imageDataRepository.createImage(image)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Log.e("CREATE_IMAGE","OnNext")},
                        {e -> Log.e("CREATE_IMAGE","OnError : ${e.localizedMessage}")}
                )
        )
    }

    // --------------------
    // LOCATION
    // --------------------

    fun getLocation(): LiveData<List<Location>>{
        return locationDataRepository.getLocation()
    }

    fun createLocation(location: Location){
        this.disposable.add(locationDataRepository.createLocation(location)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe({Log.e("CREATE_LOCATION","OnNext")},{e -> Log.e("CREATE_LOCATION","OnError : ${e.localizedMessage}")})
        )
    }
}