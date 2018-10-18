package com.openclassrooms.realestatemanager.Controller.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.facebook.stetho.Stetho
import com.openclassrooms.realestatemanager.Controller.Fragments.DetailFragment
import com.openclassrooms.realestatemanager.Controller.Fragments.ListFragment
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.toolbar.*

private const val FRAGMENT_LIST = 10
private const val FRAGMENT_DETAIL = 20

var TABLET_MODE:Boolean = false

class MainActivity : AppCompatActivity() {

    private var detailFragment: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)

        this.configureToolbar()

        this.showFragment(ListFragment.newInstance())
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

    private fun showFragment(newFragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()

        if (detailFragment == null && findViewById<FrameLayout>(R.id.fragment_view_detail) != null){
            TABLET_MODE = true
            detailFragment = DetailFragment.newInstance()
            transaction.add(R.id.fragment_view_detail, detailFragment as DetailFragment)
        }else{
            TABLET_MODE = false
        }

        transaction.replace(R.id.fragment_view, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
