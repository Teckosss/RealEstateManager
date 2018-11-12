package com.openclassrooms.realestatemanager

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.Utils.toFRDate
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Adrien Deguffroy on 11/11/2018.
 */

@RunWith(AndroidJUnit4::class)
open class FullEstateTest {

    // DATA
    private lateinit var database: RealEstateManagerDatabase

    // ESTATE DATA SET FOR TEST
    private val ESTATE_ID:Long = 1
    private val ESTATE_TYPE = "DUPLEX"
    private val ESTATE_PRICE = 209000.0
    private val ESTATE_SURFACE = 247
    private val ESTATE_ROOM_NUMBER = 6
    private val ESTATE_BATHROOM_NUMBER = 1
    private val ESTATE_BEDROOM_NUMBER = 3
    private val ESTATE_DESC = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis elementum."
    private val ESTATE_PARKS = true
    private val ESTATE_SHOPS = true
    private val ESTATE_SCHOOLS = true
    private val ESTATE_HIGHWAY = true
    private val ESTATE_STATUTE = "AVAILABLE"
    private val ESTATE_DATE = Utils.getTodayDate().toFRDate()
    private val ESTATE_SOLD_DATE = null
    private val ESTATE_AGENT = "Adrien"

    // LOCATION DATA SET FOR TEST
    private val LOCATION_ID:Long = 1
    private val LOCATION_ADDRESS = "100 Park Avenue"
    private val LOCATION_ADD_ADDRESS = "3B"
    private val LOCATION_SECTOR = "Manhattan"
    private val LOCATION_CITY = "New York"
    private val LOCATION_ZIP = "NY 10021"
    private val LOCATION_COUNTRY = "USA"
    private val LOCATION_ESTATE_ID:Long = 1


    private val ESTATE_DEMO = Estate(ESTATE_ID,ESTATE_TYPE,ESTATE_PRICE,ESTATE_SURFACE,ESTATE_ROOM_NUMBER,ESTATE_BATHROOM_NUMBER,ESTATE_BEDROOM_NUMBER,ESTATE_DESC,ESTATE_PARKS,ESTATE_SHOPS,ESTATE_SCHOOLS,ESTATE_HIGHWAY,ESTATE_STATUTE,ESTATE_DATE,ESTATE_SOLD_DATE,ESTATE_AGENT)
    private val LOCATION_DEMO = Location(LOCATION_ID,LOCATION_ADDRESS,LOCATION_ADD_ADDRESS,LOCATION_SECTOR,LOCATION_CITY,LOCATION_ZIP,LOCATION_COUNTRY,LOCATION_ESTATE_ID)
    private val IMAGE_DEMO_1 = Image(1,"content://path_1","Title 1","Desc 1", 1)
    private val IMAGE_DEMO_2 = Image(2,"content://path_2","Title 2","Desc 2", 1)
    private val IMAGE_DEMO_3 = Image(1,"content://path_3","Title 3","Desc 3", 1)

    @get:Rule
    open val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDB(){
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun closeDB(){
        this.database.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun getItemsWhenNoItemInserted() {
        val listImage = LiveDataTestUtil.getValue(this.database.imageDao().getItems(1))
        assertTrue(listImage.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetFullEstate(){
        this.database.estateDao().insertItem(ESTATE_DEMO)
        this.database.locationDao().insertItem(LOCATION_DEMO)
        this.database.imageDao().insertItem(IMAGE_DEMO_1)

        // TEST
        val fullEstate = LiveDataTestUtil.getValue(this.database.estateDao().getItemsByID(ESTATE_ID))
        assertTrue(fullEstate.estate.desc.equals(ESTATE_DESC) && fullEstate.estate.id == ESTATE_ID)
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndUpdateItems(){
        this.database.estateDao().insertItem(ESTATE_DEMO)
        this.database.imageDao().insertItem(IMAGE_DEMO_1)
        this.database.imageDao().updateItem(IMAGE_DEMO_3)

        val listImage = LiveDataTestUtil.getValue(this.database.imageDao().getItems(1))
        assertTrue(listImage.size == 1 && listImage[0].imageTitle.equals("Title 3"))
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndDeleteItem(){
        this.database.estateDao().insertItem(ESTATE_DEMO)
        this.database.imageDao().insertItem(IMAGE_DEMO_1)
        this.database.imageDao().insertItem(IMAGE_DEMO_2)

        var listImage = LiveDataTestUtil.getValue(this.database.imageDao().getItems(1))
        assertTrue(listImage.size == 2)

        this.database.imageDao().deleteItem(1)
        this.database.imageDao().deleteItem(2)

        listImage = LiveDataTestUtil.getValue(this.database.imageDao().getItems(1))
        assertTrue(listImage.isEmpty())
    }
}