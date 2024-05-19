package com.example.acs.history

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.acs.Route
import com.example.acs.login.defaultPadding
import com.example.acs.login.itemSpacing
import com.example.acs.ui.theme.ACSTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun HistoryScreen() {

    val context = LocalContext.current.applicationContext

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val tag = "MyActivity"

    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_uid", null)
    val openDrawer = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val userMail = sharedPreferences.getString("user_mail", null)
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp))
                    .width(250.dp)
                    .background(color = Color.Black.copy(alpha = 0.95f)) // Set background color
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(defaultPadding),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(Modifier.height(itemSpacing + 10.dp))
                    Text("Logged in as")
                    Text(text = userMail.toString())
                    Spacer(Modifier.height(itemSpacing +30.dp))

                }
                Column (
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                )
                {

                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(com.example.acs.login.defaultPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.Start),
                    onClick =
                    {
                        openDrawer.value = true
                    }
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                }
            }
            Text(text = "This is where your history will be displayed.")
        })

}

@Preview(showSystemUi = true)
@Composable
fun PrevSignUp(){
    ACSTheme {
        Route.HistoryScreen()
    }
}