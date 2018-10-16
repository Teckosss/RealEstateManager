package com.openclassrooms.realestatemanager.Controller.Views

import android.support.v7.widget.RecyclerView
import android.view.View
import com.openclassrooms.realestatemanager.Models.Estate
import kotlinx.android.synthetic.main.fragment_list_item.view.*

/**
 * Created by Adrien Deguffroy on 13/10/2018.
 */
class FragmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithData(estate: Estate){
        itemView.test_estate_type.setText(estate.estateType.toString())

        itemView.test_estate_price.setText("${estate.price}")
    }
}