package com.example.acs.history

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.acs.Route
import com.example.acs.ui.theme.ACSTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun HistoryScreen(onLoginClick: () -> Unit) {

    val context = LocalContext.current.applicationContext

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val tag = "MyActivity"

    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_uid", null)
    Column {

    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevSignUp(){
    ACSTheme {
        Route.HistoryScreen()
    }
}