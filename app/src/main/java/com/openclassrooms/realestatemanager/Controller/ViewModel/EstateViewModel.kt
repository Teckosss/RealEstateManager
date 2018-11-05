package com.openclassrooms.realestatemanager.Controller.ViewModel

import android.arch.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.openclassrooms.realestatemanager.Controller.Fragments.SearchFragment
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.toFRDate
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import java.sql.Array
import java.sql.Date
import java.text.SimpleDateFormat


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

    var listImagesToDeleteFromDB = ArrayList<Image>()

    var listSector : MutableLiveData<List<String>> = MutableLiveData()

    override fun onCleared() {
        super.onCleared()
        this.disposable.dispose()
    }

    private fun updateListSector(list:List<String>){
        listSector.value= list
    }

    fun getListSector(): List<String> {
        return listSector.value!!
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

    fun getEstatesBySearch(queryToConvert:String, args:ArrayList<Any>) : LiveData<List<FullEstate>>{
        val query = SimpleSQLiteQuery(queryToConvert,args.toArray())
        Log.e("GET_ESTATES_BY_SEARCH","Query to execute : ${query.sql}")
        args.forEach {
            if (it is Long) Log.e("GET_ESTATES_BY_SEARCH", "Args : ${SimpleDateFormat("dd/MM/yyyy").format(Date(it))}")
            else Log.e("GET_ESTATES_BY_SEARCH", "Args : $it")
        }
        return estateDataRepository.gesEstatesBySearch(query)
    }

    fun getEstatesByID(estateId: Long) : LiveData<FullEstate>{
        return estateDataRepository.gesEstateByID(estateId)
    }

    fun createEstate(estate:Estate, location: Location,context: Context, listImages:List<Image>) {
        this.disposable.add(estateDataRepository.createEstate(estate)
                .map { location.apply { location.estateId = it }}
                .flatMap { locationDataRepository.createLocation(location) }
                .map { listImages.forEach { it.estateId = location.estateId ; this.createImage(it) } }
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Toast.makeText(context, context.resources.getString(R.string.activity_add_estate_saved), Toast.LENGTH_SHORT).show(); Log.e("CREATE_ESTATE","OnNext")},
                        {e -> Log.e("CREATE_ESTATE","OnError : ${e.localizedMessage}")}
                )
        )
    }

    fun updateEstate(estate: Estate, location: Location, context: Context, listImage:List<Image>, listImageToDelete:ArrayList<Image>){
        this.disposable.add(estateDataRepository.updateEstate(estate)
                .flatMap { locationDataRepository.getLocationId(estate.id) }
                .map { location.apply { id = it } }
                .flatMap { locationDataRepository.updateLocation(it) }
                .map {listImage.forEach{if(it.id.toInt() != 0){ this.updateImage(it) }else{Log.e("UPDATE_ESTATE","CURRENT IMAGE : $it"); this.createImage(it) }} }
                .map { listImageToDelete.forEach { this.deleteImage(it) } }
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        { Toast.makeText(context, context.resources.getString(R.string.activity_add_estate_saved), Toast.LENGTH_SHORT).show();Log.e("UPDATE_ESTATE","OnNext")},
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

    fun getSectorList(){
        this.disposable.add(locationDataRepository.getSectorList()
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        { updateListSector(it);Log.e("GET_SECTOR_LIST","OnNext")},
                        {e -> Log.e("GET_SECTOR_LIST","OnError : ${e.localizedMessage}")})
        )
    }

    fun getLocationId(estateId: Long){
        this.disposable.add(locationDataRepository.getLocationId(estateId)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {Log.e("GET_LOCATION_ID","OnNext")},
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