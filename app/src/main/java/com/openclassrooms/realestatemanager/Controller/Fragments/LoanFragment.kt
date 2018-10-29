package com.openclassrooms.realestatemanager.Controller.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.openclassrooms.realestatemanager.R

/**
 * A simple [Fragment] subclass.
 *
 */
class LoanFragment : Fragment() {

    companion object {
        fun newInstance() : LoanFragment{
            return  LoanFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan, container, false)
    }


}
