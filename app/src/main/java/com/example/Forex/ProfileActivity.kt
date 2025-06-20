package com.example.Forex

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val etFirstName: EditText = findViewById(R.id.etFirstName)
        val etLastName: EditText = findViewById(R.id.etLastName)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val btnSave: Button = findViewById(R.id.btnSave)

        val userId = intent.getStringExtra("USER_ID")

        if (userId != null) {
            database.child("users").child(userId).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userMap = task.result?.value as? Map<*, *>
                    etFirstName.setText(userMap?.get("firstName") as? String ?: "")
                    etLastName.setText(userMap?.get("lastName") as? String ?: "")
                    etEmail.setText(userMap?.get("email") as? String ?: "")
                } else {
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSave.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val email = etEmail.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userId != null) {
                val userUpdates = mapOf<String, Any>(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to email
                )

                database.child("users").child(userId).updateChildren(userUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
