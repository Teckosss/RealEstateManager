package com.openclassrooms.realestatemanager.Controller.Views

import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_add_item.view.*
import java.lang.ref.WeakReference

/**
 * Created by Adrien Deguffroy on 10/10/2018.
 */
class ActivityAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var callbackWeakRef:WeakReference<ActivityAddAdapter.Listener>


    fun updateWithData(image:Uri, callback:ActivityAddAdapter.Listener){
        val glide:RequestManager = Glide.with(itemView)

        glide.load(image).apply(RequestOptions().centerCrop()).into(itemView.horizontal_item_view_image)

        itemView.horizontal_item_delete.setOnClickListener{this.onClick()}
        this.callbackWeakRef = WeakReference(callback)
    }

    fun onClick(){
        val callback = callbackWeakRef.get()
        callback!!.onClickDeleteButton(adapterPosition)
    }
}