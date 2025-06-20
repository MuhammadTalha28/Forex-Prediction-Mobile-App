package com.example.Forex

import android.os.Bundle

class CausesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_causes, findViewById(R.id.content_frame))

        setNavigationViewItemChecked(R.id.nav_causes)
    }
}
