package com.openclassrooms.realestatemanager.Controller.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.facebook.stetho.Stetho
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)

        this.configureToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when(id){
            R.id.menu_add -> launchActivity(AddActivity::class.java)
            R.id.menu_edit -> launchActivity(EditActivity::class.java)
            R.id.menu_search -> launchActivity(SearchActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
    }

   private fun <T>launchActivity(mClass:Class<T>){
       val intent = Intent(this, mClass)
       startActivity(intent)
   }
}
