package com.example.acs.history

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.acs.components.HeaderText
import com.example.acs.login.defaultPadding
import com.example.acs.ui.theme.ACSTheme
import com.google.firebase.firestore.FirebaseFirestore

val defaultPadding = 16.dp
val itemSpacing = 8.dp
@SuppressLint("StaticFieldLeak")
val db = FirebaseFirestore.getInstance()
val tag_db = "DatabaseActivity"

fun getCollectionsAsMap(callback: (HashMap<String, Any?>) -> Unit) {
    val hashMap = HashMap<String, Any?>()
    db.collection("access_history")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
//                Log.d(tag_db, "${document["date"]} => ${document["room_id"]}")
                hashMap[document["date"].toString()] = document["room_id"].toString()
            }
            callback(hashMap)
        }
        .addOnFailureListener { exception ->
            Log.d(tag_db, "Error getting documents: ", exception)
        }
}

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun HistoryScreen(onAccessClick: () -> Unit) {

    val context = LocalContext.current.applicationContext

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
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = Color.White.copy(alpha = 0.2f))
                            .padding(1.dp)
                            .clickable {
                                onAccessClick()
                            }
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Open Access Menu",
                            modifier = Modifier
                                .padding(8.dp))
                        val text = Text(
                            text = "Панель доступа           ",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                        }

                    }
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
                HeaderText(
                    text = "История доступа",
                    modifier = Modifier
                        .padding(vertical = defaultPadding)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    var accessHistory = mutableStateOf(HashMap<String, Any?>())

                    getCollectionsAsMap { populatedHashMap ->
                        accessHistory.value = populatedHashMap
                        
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (accessHistory.value.isNotEmpty()) {
                            // Check if accessHistory has data to avoid empty list
                            for ((key, value) in accessHistory.value) {
                                Text("Key: $key, Value: $value")
                            }
                        } else {
                            // Optional: Show loading indicator while data is fetched
                            Text("Loading access history...")
                        }
                    }
                }
                
            }
            LaunchedEffect(openDrawer.value) {
                if (openDrawer.value) {
                    drawerState.open()
                    openDrawer.value = false // Reset state after opening
                }
            }
        })

}

@Preview(showSystemUi = true)
@Composable
fun PrevHistory(){
    ACSTheme {
        Route.HistoryScreen()
    }
}