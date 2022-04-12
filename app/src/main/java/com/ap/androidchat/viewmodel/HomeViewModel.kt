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
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel:ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth
    private val _isLoggedIn = MutableLiveData<Boolean>(true)
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private lateinit var database: DatabaseReference

    fun logout(){
        auth.signOut()
        _isLoggedIn.value = false
    }

    fun getPersons(): ArrayList<User> {
        var listUsersReal =  ArrayList<User>()
        val user = User()
        database = FirebaseDatabase.getInstance().reference
        val chats = database.child("users")

        chats.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val listUsers =  ArrayList<User>()
                if (snapshot.exists()){
                    snapshot.children.forEach { it ->
                        user.name = it.child("name").value as String?
                        user.profilePic = it.child("profilePic").value as String
                        listUsers.add( user )
                    }
                    println(listUsers)
                    listUsersReal = listUsers
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("loadChat:onCancelled", error.toException())
            }
        })
        println(listUsersReal)
        return listUsersReal
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
