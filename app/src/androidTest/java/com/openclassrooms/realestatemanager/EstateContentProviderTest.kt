package com.openclassrooms.realestatemanager

import android.arch.persistence.room.Room
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Provider.EstateContentProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Adrien Deguffroy on 13/11/2018.
 */

@RunWith(AndroidJUnit4::class)
class EstateContentProviderTest {

    // FOR DATA
    private lateinit var mContentResolver:ContentResolver

    // DATA SET FOR TEST
    private val ESTATE_ID:Long = 1
    private val ESTATE_ID_2:Long = 9999

    @Before
    fun setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),RealEstateManagerDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        mContentResolver = InstrumentationRegistry.getContext().contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted(){
        val cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider().URI_ESTATE, ESTATE_ID_2),null,null,null,null)
        assertNotNull(cursor)
        assertEquals(0, cursor.count)
        cursor.close()
    }

   @Test
    fun insertAndGetItem(){
        // ADDING DEMO ESTATE
        mContentResolver.insert(EstateContentProvider().URI_ESTATE, generateEstate())

        // TEST
        val cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider().URI_ESTATE, ESTATE_ID), null,null,null,null)
        assertNotNull(cursor)
        assertEquals(1,cursor.count)
        assertEquals(true,cursor.moveToFirst())
        assertEquals("Adrien",cursor.getString(cursor.getColumnIndexOrThrow("estateAgent")))
    }

    private fun generateEstate():ContentValues{
        val values = ContentValues()
        values.put("estateType","FLAT")
        values.put("price",150000.0)
        values.put("surface",150)
        values.put("roomNumber",5)
        values.put("bathroomNumber",1)
        values.put("bedroomNumber",3)
        values.put("desc","Beautiful estate")
        values.put("parks",true)
        values.put("shops",true)
        values.put("schools",false)
        values.put("highway",true)
        values.put("estateStatute","AVAILABLE")
        values.put("entryDate",Date().time)
        values.put("soldDate",0L)
        values.put("estateAgent","Adrien")
        return values
    }
}