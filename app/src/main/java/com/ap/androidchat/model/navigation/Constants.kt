package com.ap.androidchat.model.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = "home"
        ),
        BottomNavItem(
            label = "Messages",
            icon = Icons.Filled.Email,
            route = "chats"
        ),
        BottomNavItem(
            label = "Logout",
            icon = Icons.Filled.ExitToApp,
            route = "logout",
        )
    )
}