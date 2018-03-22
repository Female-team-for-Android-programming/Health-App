package com.example.user.healthcalendar

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.user.healthcalendar.Fragments.*
import com.example.user.healthcalendar.R.id.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    val fcalendar = FragmentCalendar()
    val fdoctors = FragmentDoctors()
    val fmeds = FragmentMeds()
    val fexam = FragmentExam()
    val fjournal = FragmentJournal()
    val fmanage = FragmentManage()
    val fhome = FragmentHome()
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //var fdoctors : FragmentDoctors = FragmentDoctors()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val ftrans = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.nav_home -> {
                ftrans.replace(R.id.container, fhome)
            }
            R.id.nav_calendar -> {
                ftrans.replace(R.id.container, fcalendar)
            }
            R.id.nav_doctors -> {
                ftrans.replace(R.id.container, fdoctors)
            }
            R.id.nav_meds -> {
                ftrans.replace(R.id.container, fmeds)
            }
            R.id.nav_exam -> {
                ftrans.replace(R.id.container, fexam)
            }
            R.id.nav_journal -> {
                ftrans.replace(R.id.container, fjournal)
            }
            R.id.nav_manage -> {
                ftrans.replace(R.id.container, fmanage)
            }
        }
        ftrans.commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
