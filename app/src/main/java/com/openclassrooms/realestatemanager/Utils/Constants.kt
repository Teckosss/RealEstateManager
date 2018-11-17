package com.openclassrooms.realestatemanager.Utils

import android.Manifest

/**
 * Created by Adrien Deguffroy on 24/10/2018.
 */
object Constants {

    const val VIEW_HOLDER_ACTION_ADD = "ADD"
    const val VIEW_HOLDER_ACTION_EDIT = "EDIT"
    const val VIEW_HOLDER_ACTION_DETAIL = "DETAIL"

    const val MAP_FRAGMENT_DEFAULT_ZOOM = 8F // GLOBAL MAP
    const val DETAIL_FRAGMENT_DEFAULT_ZOOM = 17F // STATIC DETAIL MAP

    const val FRAGMENT_TAG_KEY = "FRAGMENT_TAG_KEY"

    const val FRAGMENT_LIST = "LIST"
    const val FRAGMENT_SEARCH = "SEARCH"
    const val FRAGMENT_MAP = "MAP"
    const val FRAGMENT_LOAN = "LOAN"

    const val VIEW_MODEL_ACTION_CREATE = "CREATE"
    const val VIEW_MODEL_ACTION_EDIT = "EDIT"

    const val OVERLAY_WIDTH_PERCENT = 0.85
    const val OVERLAY_HEIGHT_PERCENT = 0.85

    const val PERMS = Manifest.permission.READ_EXTERNAL_STORAGE
    const val PERM_CAMERA = Manifest.permission.CAMERA
    const val PERM_WRITE_EXT = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PERM_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val PERM_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    const val RC_IMAGE_PERMS = 100
    const val RC_CHOOSE_PHOTO = 200
    const val RC_TAKE_PHOTO = 300
}