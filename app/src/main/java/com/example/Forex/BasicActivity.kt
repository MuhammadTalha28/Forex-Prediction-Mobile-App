package com.example.Forex

import android.os.Bundle

class BasicActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_basic, findViewById(R.id.content_frame))

        setNavigationViewItemChecked(R.id.nav_basic)
    }
}
