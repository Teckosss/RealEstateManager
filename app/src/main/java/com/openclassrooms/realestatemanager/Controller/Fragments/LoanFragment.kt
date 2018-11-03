package com.openclassrooms.realestatemanager.Controller.Fragments


import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.TextView
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.estate_info.*
import kotlinx.android.synthetic.main.fragment_loan.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setOnClickListener()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun setOnClickListener(){
        loan_term.setOnClickListener{displayPopupMenu()}
        loan_calculate.setOnClickListener { calculate() }
    }

    private fun displayPopupMenu(){
        val popupMenu = PopupMenu(this.context, loan_term)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_loan_term, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item -> loan_term.setText(item.title); true}
        popupMenu.show()
    }

    // ---------------------
    // ACTION
    // ---------------------

    private fun calculate(){
        var canCalculate = false
        val amount = loan_amount.text.toString().toIntOrNull()
        val downPayment = loan_down.text.toString().toIntOrNull()
        val term = loan_term.text.toString().toDoubleOrNull()
        val interest = loan_interest.text.toString().toDoubleOrNull()

        when{
            loan_amount.text.isNullOrEmpty() || loan_term.text.isNullOrEmpty() || loan_interest.text.isNullOrEmpty() -> {
                canCalculate = false
                if (loan_amount.text.isNullOrEmpty()){
                    loan_amount_layout.error = resources.getString(R.string.loan_error)
                }
                if (loan_term.text.isNullOrEmpty()){
                    loan_term_layout.error = resources.getString(R.string.loan_error)
                }
                if (loan_interest.text.isNullOrEmpty()){
                    loan_interest_layout.error = resources.getString(R.string.loan_error)
                }
            }
            else -> {
                canCalculate = true
                loan_amount_layout.error = null
                loan_term_layout.error = null
                loan_interest_layout.error = null
            }
        }

        if (canCalculate){
            val result = (if (downPayment != null) (amount!! -(downPayment)) else amount)!! * ((interest!! / (100)) / (12)) / (1 - Math.pow( 1 + ((interest / 100) / 12), -term!! *12))
            val totalPrice = 12 * term * result - amount!!
            loan_monthly.setText(String.format("%.2f",result),TextView.BufferType.EDITABLE)
            loan_total.setText(String.format("%.2f",totalPrice), TextView.BufferType.EDITABLE)
        }
    }
}
