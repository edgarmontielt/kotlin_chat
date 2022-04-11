package com.ap.androidchat.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ap.androidchat.R
import com.ap.androidchat.model.navigation.Constants
import com.ap.androidchat.viewmodel.HomeViewModel

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = HomeViewModel()

        viewModel.isLoggedIn.observe(this, Observer {
            if(!it){
                this.finish()
            }
        })

        setContent {

            val navController = rememberNavController()
            Surface(color = Color.White) {
                Scaffold(bottomBar = {
                    BottomNavigationBar(navController = navController)
                }, content = {paddingValues,  ->
                    NavHostContainer(
                        navController = navController,
                        paddingValues = paddingValues
                ) {
                        viewModel.logout()
                    }})
            }
        }
    }
}

@Composable
fun ChatsActivityContent(logout:()->Unit){

}


@Composable
fun HomeScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
    horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home", color = Color.Black)
    }    
}

@Composable
fun ChatsScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Chats", color = Color.Black)
    }
}


// Navigation Components
@Composable
fun NavHostContainer(
    navController: NavHostController,
    paddingValues: PaddingValues,
    logout:()->Unit
){
    NavHost(
        navController = navController,
        startDestination = "home",

        modifier = Modifier.padding(paddingValues = paddingValues),
        builder = {
            composable("home") {
                HomeScreen()
            }
            composable("chats") {
                ChatsScreen()
            }
            composable("logout") {
                logout()
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.secondaryDarkColor)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute = navBackStackEntry?.destination?.route

        Constants.BottomNavItems.forEach { navIt ->
            BottomNavigationItem(selected = currentRoute == navIt.route,
                onClick = {
                    navController.navigate(navIt.route)
                },
                icon = {
                    Icon(
                        imageVector = navIt.icon,
                        contentDescription = navIt.label,
                        tint = Color.White
                    )},
                label = {
                    Text(text = navIt.label, style = TextStyle(color = Color.White))
                },
                alwaysShowLabel = false
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatsActivityContent() {
    ChatsActivityContent {

    }
}