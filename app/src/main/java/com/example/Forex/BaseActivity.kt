package com.example.Forex

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.widget.ImageButton


open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navDrawerButton: ImageButton = findViewById(R.id.nav_drawer_button)
        navDrawerButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val profileButton: ImageButton = findViewById(R.id.profile_button)
        profileButton.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            R.id.nav_causes -> {
                startActivity(Intent(this, CausesActivity::class.java))
                finish()
            }
            R.id.nav_Strategies -> {
                startActivity(Intent(this, StrategiesActivity::class.java))
                finish()
            }
            R.id.nav_basic -> {
                startActivity(Intent(this, BasicActivity::class.java))
                finish()
            }
            R.id.nav_testing -> {
                startActivity(Intent(this, TestingActivity::class.java))
                finish()
            }
            R.id.nav_aboutus -> {
                startActivity(Intent(this, AboutUsActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (this !is MainActivity) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }

    protected fun setNavigationViewItemChecked(itemId: Int) {
        navigationView.setCheckedItem(itemId)
    }
}
