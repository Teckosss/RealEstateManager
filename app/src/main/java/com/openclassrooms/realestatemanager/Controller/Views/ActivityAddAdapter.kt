package com.openclassrooms.realestatemanager.Controller.Views

import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.R

/**
 * Created by Adrien Deguffroy on 10/10/2018.
 */
class ActivityAddAdapter(private val pictures:List<Uri>, val callback:Listener) : RecyclerView.Adapter<ActivityAddViewHolder>() {

    interface Listener{
        fun onClickDeleteButton(position:Int)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ActivityAddViewHolder {
       return ActivityAddViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.activity_add_item, p0, false))
    }

    override fun getItemCount(): Int {
       return pictures.size
    }

    fun getImage(position: Int):Int{
        return this.getImage(position)
    }

    override fun onBindViewHolder(p0: ActivityAddViewHolder, p1: Int) {
        p0.updateWithData(this.pictures[p1],this.callback)
    }
}