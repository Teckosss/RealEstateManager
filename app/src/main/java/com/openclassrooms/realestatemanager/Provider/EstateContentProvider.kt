package com.openclassrooms.realestatemanager.Provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Estate

/**
 * Created by Adrien Deguffroy on 13/11/2018.
 */
class EstateContentProvider : ContentProvider() {

    // FOR DATA
    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    val TABLE_NAME = Estate::class.java.simpleName
    var URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
       if (context != null && p1 != null){
           Log.e("EstateContentProvider","ContentValues : $p1")
           val index = RealEstateManagerDatabase.getInstance(context).estateDao().insertItem(Estate().fromContentValues(p1))
           if (index != 0L){
               context.contentResolver.notifyChange(p0,null)
               return ContentUris.withAppendedId(p0,index)
           }
       }

        throw IllegalArgumentException("Failed to insert row into $p0")
    }

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {
        if (context != null){
            val index:Long = ContentUris.parseId(p0)
            val cursor = RealEstateManagerDatabase.getInstance(context).estateDao().getItemsWithCursor(index)
            cursor.setNotificationUri(context.contentResolver,p0)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $p0")
    }

    override fun onCreate(): Boolean {
      return true
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
       if (context != null && p1 != null){
           val count:Int = RealEstateManagerDatabase.getInstance(context).estateDao().updateItem(Estate().fromContentValues(p1))
           context.contentResolver.notifyChange(p0,null)
           return count
       }

        throw IllegalArgumentException("Failed to update row into $p0")
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
       throw IllegalArgumentException("You can't delete anything")
    }

    override fun getType(p0: Uri?): String {
       return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }
}