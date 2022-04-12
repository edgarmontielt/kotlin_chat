package com.ap.androidchat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ap.androidchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthViewModel:ViewModel() {
    private var auth:FirebaseAuth = Firebase.auth
    private var currentUser = auth.currentUser
    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    val isLoggedIn: LiveData<Boolean>
    get() = _isLoggedIn
    private var message = ""
    var isError = false
    private var database: DatabaseReference = Firebase.database.reference


    init{
        if (currentUser!=null){
            _isLoggedIn.value = true
        }
    }

    fun auth(email:String,password:String,isLogin:Boolean, name: String, profilePic: String){
        if(isLogin){
            login(
                email = email,
                password = password)
        }else{
            register(
                email = email,
                password = password,
                name = name,
                profilepic = profilePic)
        }
    }


    private fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{ result ->
                if(result.isSuccessful){
                    currentUser = auth.currentUser
                    _isLoggedIn.value = true
                    Log.d("AUTH","Session Inicialized: "+currentUser.toString())
                }else{
                    Log.d("AUTH","Sesion no iniciada: ",result.exception)
                    message = "El inicio de sesión falló"
                    isError = true
                }
            }
    }

    private fun register(email:String,password:String, name:String, profilepic:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { result ->
                if(result.isSuccessful){
                    currentUser = auth.currentUser
                    _isLoggedIn.value = true
                    println(result.result.user?.uid)
                    Log.d("AUTH","Usuario registrado: "+currentUser.toString())
                    val id = result.result.user?.uid
                    val user = User( name, profilepic)
                    database.child("users").child(id!!).setValue(user)

                }else{
                    Log.d("AUTH","Fallo al registrar usuario: ",result.exception)
                    message = "No"
                    isError = true
                }
            }
    }
}