package com.ap.androidchat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ap.androidchat.model.ChatMessage
import com.ap.androidchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeViewModel:ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val _isLoggedIn = MutableLiveData<Boolean>(true)
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private var database: DatabaseReference = Firebase.database.reference


    fun logout(){
        auth.signOut()
        _isLoggedIn.value = false
    }


    val chatListener = object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val chat = snapshot.getValue<User>()
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("loadChat:onCancelled", error.toException())
        }
    }

    fun setMessage(title:String, content:String, date:Date) {
        FirebaseDatabase.getInstance()
            .getReference("chats")
            .push()
            .setValue(ChatMessage(title, content, date),
                FirebaseAuth.getInstance()
                    .currentUser
                    ?.displayName
            )
    }
}