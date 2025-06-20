package com.example.Forex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

data class User(val userId: String, val firstName: String, val lastName: String, val email: String)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val _loginStatus = MutableLiveData<Result<User>>()
    val loginStatus: LiveData<Result<User>> get() = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid
                        if (userId != null) {
                            database.child("users").child(userId).get().addOnCompleteListener { dataTask ->
                                if (dataTask.isSuccessful) {
                                    val userMap = dataTask.result?.value as? Map<*, *>
                                    val firstName = userMap?.get("firstName") as? String ?: ""
                                    val lastName = userMap?.get("lastName") as? String ?: ""
                                    val email = userMap?.get("email") as? String ?: ""
                                    _loginStatus.postValue(Result.success(User(userId, firstName, lastName, email)))
                                } else {
                                    _loginStatus.postValue(Result.failure(dataTask.exception ?: Exception("Failed to retrieve user data")))
                                }
                            }
                        }
                    } else {
                        _loginStatus.postValue(Result.failure(task.exception ?: Exception("Authentication failed")))
                    }
                }
            } catch (e: Exception) {
                _loginStatus.postValue(Result.failure(e))
            }
        }
    }
}
