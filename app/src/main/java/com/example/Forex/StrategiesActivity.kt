package com.example.Forex

import android.os.Bundle

class StrategiesActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_strategies, findViewById(R.id.content_frame))

        setNavigationViewItemChecked(R.id.nav_Strategies)
    }
}
