package com.example.Forex

import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.FirebaseDatabase

class MainActivity : BaseActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_main, findViewById(R.id.content_frame))

        userId = intent.getStringExtra("USER_ID") ?: ""

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val images = listOf(
            R.drawable.sell1,
            R.drawable.sell2,
            R.drawable.sell3,
            R.drawable.buy,
        )

        adapter = ViewPagerAdapter(this, images)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Setup tab if needed
        }.attach()

        // Display user name
        val tvUserName: TextView = findViewById(R.id.tvUserName)
        if (userId.isNotEmpty()) {
            val database = FirebaseDatabase.getInstance().reference
            database.child("users").child(userId).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userMap = task.result?.value as? Map<*, *>
                    val firstName = userMap?.get("firstName") as? String ?: ""
                    tvUserName.text = "Hello, $firstName!"
                } else {
                    tvUserName.text = "Hello!"
                }
            }
        }
    }
}
