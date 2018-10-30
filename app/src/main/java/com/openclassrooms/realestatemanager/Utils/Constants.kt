package com.openclassrooms.realestatemanager.Utils

import android.Manifest

/**
 * Created by Adrien Deguffroy on 24/10/2018.
 */
object Constants {

    const val VIEW_HOLDER_ACTION_ADD = "ADD"
    const val VIEW_HOLDER_ACTION_EDIT = "EDIT"
    const val VIEW_HOLDER_ACTION_DETAIL = "DETAIL"

    const val OVERLAY_WIDTH_PERCENT = 0.85
    const val OVERLAY_HEIGHT_PERCENT = 0.85

    const val PERMS = Manifest.permission.READ_EXTERNAL_STORAGE
    const val PERM_CAMERA = Manifest.permission.CAMERA
    const val PERM_WRITE_EXT = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val RC_IMAGE_PERMS = 100
    const val RC_CHOOSE_PHOTO = 200
    const val RC_TAKE_PHOTO = 300
}