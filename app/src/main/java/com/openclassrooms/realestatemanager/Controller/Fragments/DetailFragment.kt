package com.openclassrooms.realestatemanager.Controller.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    companion object {
        fun newInstance():DetailFragment{
            return DetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.retrieveDatabaseId()
    }

    private fun retrieveDatabaseId(){
        val databaseId = arguments?.get(DATABASE_ID)
        Log.e("DETAIL_FRAGMENT","Id retrieved : $databaseId")
    }
}
