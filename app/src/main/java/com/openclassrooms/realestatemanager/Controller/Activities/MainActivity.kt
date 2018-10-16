package com.openclassrooms.realestatemanager.Controller.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.facebook.stetho.Stetho
import com.openclassrooms.realestatemanager.Controller.Fragments.ListFragment
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.toolbar.*

private const val FRAGMENT_LIST = 10
private const val FRAGMENT_DETAIL = 20

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)

        this.configureToolbar()

        showFragment(FRAGMENT_LIST)
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

    // ---------------------
    // FRAGMENTS
    // ---------------------

    private fun showFragment(fragmentId :Int){
        lateinit var newFragment:Fragment
        when(fragmentId){
            FRAGMENT_LIST -> {
                newFragment = ListFragment.newInstance()
                Log.e("MainActivity","Requested : Display ListFragment")
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.fragment_view, newFragment)
        transaction.addToBackStack(null)
        // Commit the transaction
        transaction.commit()

    }
}
