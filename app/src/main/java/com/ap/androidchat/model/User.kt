package com.ap.androidchat.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var name:String? = null,
    var profilePic: String? = null,
) {}