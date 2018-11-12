package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.Utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Adrien Deguffroy on 27/10/2018.
 */
class UtilsUnitTest {

    private val nowDate = SimpleDateFormat("dd/MM/yyyy").format(Date())

    @Test
    fun convertDollarToEuro() {
        assertEquals(121800, Utils.convertDollarToEuro(150000))
    }

    @Test
    fun convertEuroToDollar() {
        assertEquals(171300, Utils.convertEuroToDollar(150000))
    }

    @Test
    fun getTodayDate() {
        assertEquals(nowDate, Utils.getTodayDate())
    }
}