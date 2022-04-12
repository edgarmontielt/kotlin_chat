package com.ap.androidchat.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ap.androidchat.R
import com.ap.androidchat.model.DataProvider
import com.ap.androidchat.model.User
import com.ap.androidchat.model.navigation.Constants
import com.ap.androidchat.viewmodel.HomeViewModel

class ChatsActivity : AppCompatActivity() {

    private val viewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    }
                })
            }
        }
    }
}

@Composable
fun ChatsActivityContent(logout:()->Unit){

}


// Screen Components

@ExperimentalCoilApi
@Composable
fun HomeScreen() {

    val viewModel = HomeViewModel()
    val persons = viewModel.getPersons()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(40.dp),
    horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Home", color = Color.Black,
            style = TextStyle(fontSize = 32.sp,
                fontWeight = FontWeight.Bold))

        ListChat(users = persons)

    }    
}

@ExperimentalCoilApi
@Composable
fun ListChat(users: ArrayList<User>) {
    Column( modifier = Modifier.padding(top=20.dp)) {
        users.forEach {
            println(it)
            MessageCard(user = it)
        }
    }

}

@ExperimentalCoilApi
@Composable
fun MessageCard(user: User){
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(painter = rememberImagePainter(user.profilePic.toString()), contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colors.secondary, CircleShape),
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = user.name.toString(),
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}

@Composable
fun ChatsScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Chats", color = Color.Black,
            style = TextStyle(fontSize = 32.sp,
                fontWeight = FontWeight.Bold))


    }
}


// Navigation Components
@ExperimentalCoilApi
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