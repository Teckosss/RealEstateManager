package com.openclassrooms.realestatemanager

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.openclassrooms.realestatemanager.Utils.Utils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Adrien Deguffroy on 12/11/2018.
 */

@RunWith(AndroidJUnit4::class)
class InternetConnectionTest {

    @Test
    fun checkInternetConnection() {
        assertEquals(true, Utils.isInternetAvailable(InstrumentationRegistry.getContext()))
    }
}