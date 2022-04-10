package com.ap.androidchat.view

import android.content.Intent
import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.ap.androidchat.R
import com.ap.androidchat.view.ChatsActivity
import com.ap.androidchat.viewmodel.AuthViewModel

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = AuthViewModel()

        viewModel.isLoggedIn.observe(this, Observer {
            if(it){
                val intent = Intent(this, ChatsActivity::class.java)

                startActivity(intent)
            }
        })

        setContent {
            LoginScreen() { email, password, isLogin ->
                println(email + "\n" +  password)
                viewModel.auth(email = email, password = password, isLogin = isLogin)
            }
        }
    }
}

@Composable
fun LoginScreen(auth:(email: String, password: String, isLogin:Boolean) -> Unit) {
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val isLogin = remember {
        mutableStateOf(true)
    }

    Scaffold(backgroundColor = colorResource(id = R.color.secondaryDarkColor)) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        )
        {
            Image(painter = painterResource(id = R.drawable.social), contentDescription = "image")
            Card(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 70.dp,),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                        .height(120.dp)
                ) {

                  Row() {
                      if (isLogin.value) {
                          Text(text = "Login", fontWeight = FontWeight.Medium, fontSize = 32.sp)
                      } else {
                          Text(text = "Register", fontWeight = FontWeight.Medium, fontSize = 32.sp)
                      }
                      Spacer(modifier = Modifier.width(64.dp))
                      Switch(checked = isLogin.value, onCheckedChange = {
                          isLogin.value = it
                      })
                  }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        EmailText(
                            value = email.value){  newValue ->
                            email.value = newValue
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PasswordText(value = password.value){ newValue ->
                            password.value = newValue
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { auth(email.value, password.value, isLogin.value)}) {
                            if(isLogin.value){
                                Text("Iniciar sesión")
                            }else{
                                Text("Registrate")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EmailText(value: String, changed: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = changed,
        label = { Text("Email") },
        placeholder = { Text(text = "Escribe tu email")},
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun PasswordText(value: String, changed: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = changed,
        label = { Text("Password") },
        placeholder = { Text(text = "Escribe tu password")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthActivity() {
    LoginScreen { email, password, isLogin ->

    }
}