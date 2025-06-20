package com.example.Forex

import android.os.Bundle


class AboutUsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_about_us, findViewById(R.id.content_frame))

        setNavigationViewItemChecked(R.id.nav_aboutus)
    }
}
