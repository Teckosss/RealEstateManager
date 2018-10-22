package com.openclassrooms.realestatemanager.Controller.Views

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list_item.view.*

/**
 * Created by Adrien Deguffroy on 13/10/2018.
 */
class FragmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithData(estate: FullEstate, position:Int){
        val glide: RequestManager = Glide.with(itemView)

        if (estate.images.isNotEmpty()){
            glide.load(Uri.parse(estate.images[0].imagePath)).apply(RequestOptions().centerCrop()).into(itemView.list_item_main_pic)
        }else{
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions().centerCrop()).into(itemView.list_item_main_pic)
        }

        itemView.list_item_type.text = estate.estate.estateType.toString()

        itemView.list_item_city.text = "${estate.location.city}"

        itemView.list_item_price.text = "${estate.estate!!.price}"
    }
}