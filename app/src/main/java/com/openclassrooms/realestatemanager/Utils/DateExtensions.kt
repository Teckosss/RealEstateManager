package com.openclassrooms.realestatemanager.Utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Adrien Deguffroy on 05/11/2018.
 */
private val BASE_FORMAT = SimpleDateFormat("dd/MM/yyyy")

fun String.toFRDate() = BASE_FORMAT.parse(this)

fun Date.toFRString() = BASE_FORMAT.format(this)

fun Long.toFRDate() = Date(this)