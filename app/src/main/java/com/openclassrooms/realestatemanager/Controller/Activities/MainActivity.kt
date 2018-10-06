package com.openclassrooms.realestatemanager.Controller.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }
}
