package com.openclassrooms.realestatemanager.Controller.ViewModel

import android.arch.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
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

    var locationId : MutableLiveData<Long> = MutableLiveData()

    var listImagesToSave = ArrayList<Image>()

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

    fun updateLocationId(newId:Long){
        locationId.value = newId
    }

    fun getLocationId():Long{
        return locationId.value!!
    }

    // --------------------
    // ESTATE
    // --------------------

    fun getEstates(): LiveData<List<FullEstate>> {
        return estateDataRepository.getEstates()
    }

    fun getEstatesByID(estateId: Long) : LiveData<FullEstate>{
        return estateDataRepository.gesEstateByID(estateId)
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

    fun updateEstate(estate: Estate){
        this.disposable.add(estateDataRepository.updateEstate(estate)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        { Log.e("UPDATE_ESTATE","OnNext")},
                        {e -> Log.e("UPDATE_ESTATE","OnError : ${e.localizedMessage}")}
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

    fun updateImage(image: Image){
        this.disposable.add(imageDataRepository.updateImage(image)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Log.e("UPDATE_IMAGE","OnNext")},
                        {e -> Log.e("UPDATE_IMAGE","OnError : ${e.localizedMessage}")}
                )
        )
    }

    fun deleteImage(image: Image){
        this.disposable.add(imageDataRepository.deleteImage(image)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Log.e("DELETE_IMAGE","OnNext")},
                        {e -> Log.e("DELETE_IMAGE","OnError : ${e.localizedMessage}")}
                )
        )
    }

    // --------------------
    // LOCATION
    // --------------------

    fun getLocation(estateId: Long): LiveData<List<Location>>{
        return locationDataRepository.getLocation(estateId)
    }

    fun getLocationId(estateId: Long){
        this.disposable.add(locationDataRepository.getLocationId(estateId)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {id -> updateLocationId(id);Log.e("GET_LOCATION_ID","OnNext")},
                        {e -> Log.e("GET_LOCATION_ID","OnError : ${e.localizedMessage}")})
        )
    }

    fun createLocation(location: Location){
        this.disposable.add(locationDataRepository.createLocation(location)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Log.e("CREATE_LOCATION","OnNext")},
                        {e -> Log.e("CREATE_LOCATION","OnError : ${e.localizedMessage}")})
        )
    }

    fun updateLocation(location: Location){
        this.disposable.add(locationDataRepository.updateLocation(location)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Log.e("UPDATE_LOCATION","OnNext")},
                        {e -> Log.e("UPDATE_LOCATION","OnError : ${e.localizedMessage}")})
        )
    }
}