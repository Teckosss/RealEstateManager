package com.openclassrooms.realestatemanager.Di

import android.content.Context
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import com.openclassrooms.realestatemanager.Controller.ViewModel.ViewModelFactory
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Adrien Deguffroy on 15/10/2018.
 */
object Injection {

    fun provideEstateDataSource(context: Context): EstateDataRepository {
        val database = RealEstateManagerDatabase.getInstance(context)
        return EstateDataRepository(database)
    }

    fun provideImageDataSource(context: Context): ImageDataRepository {
        val database = RealEstateManagerDatabase.getInstance(context)
        return ImageDataRepository(database)
    }

    fun provideLocationDataSource(context: Context) : LocationDataRepository{
        val database = RealEstateManagerDatabase.getInstance(context)
        return  LocationDataRepository(database)
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceEstate = provideEstateDataSource(context)
        val dataSourceImage = provideImageDataSource(context)
        val dataSourceLocation = provideLocationDataSource(context)
        return ViewModelFactory(dataSourceEstate, dataSourceImage,dataSourceLocation, provideSubscriberOn(), providerObserverOn())
    }

    fun provideSubscriberOn() = Schedulers.io()

    fun providerObserverOn() = AndroidSchedulers.mainThread()
}