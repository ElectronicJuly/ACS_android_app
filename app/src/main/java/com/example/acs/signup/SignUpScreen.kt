package com.example.acs.signup

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.acs.BiometricAuthenticator
import com.example.acs.Route
import com.example.acs.components.HeaderText
import com.example.acs.components.LoginTextField
import com.example.acs.login.SaveUserData
import com.example.acs.ui.theme.ACSTheme
import com.google.firebase.firestore.FirebaseFirestore

val defaultPadding = 16.dp
val itemSpacing = 8.dp
@Composable
fun SignUpScreen(onLoginClick: () -> Unit)  {

    val openDrawer = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val (idDoor, onIdDoorChange) = rememberSaveable {
        mutableStateOf("")
    }

    val isFieldsNotEmpty = idDoor.isNotEmpty()
    val context = LocalContext.current.applicationContext
    val biometricAuthenticator = BiometricAuthenticator(context)

    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_uid", null)
    val userMail = sharedPreferences.getString("user_mail", null)
    var message by remember {
        mutableStateOf("")
    }
    val tag = "MyActivity"
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(250.dp)
                    .background(color = Color.Black.copy(alpha = 0.8f)) // Set background color
            ) {
                // Your sidebar content goes here
                Text("This is the sidebar content")
            }
        },
        content = @androidx.compose.runtime.Composable {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(com.example.acs.login.defaultPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
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
                LaunchedEffect(openDrawer.value) {
                    if (openDrawer.value) {
                        drawerState.open()
                        openDrawer.value = false // Reset state after opening
                    }
                }
                HeaderText(
                    text = "Доступ",
                    modifier = Modifier
                        .padding(vertical = com.example.acs.login.defaultPadding)
                )
                LoginTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = idDoor,
                    onValueChange = onIdDoorChange,
                    labelText = "Номер",
                    leadingIcon = Icons.Default.ExitToApp
                )
                Spacer(Modifier.height(com.example.acs.login.itemSpacing + 10.dp))
                val activity = LocalContext.current as FragmentActivity
                Button(
                    modifier = Modifier.fillMaxWidth(), enabled = isFieldsNotEmpty,
                    onClick = {

                        biometricAuthenticator.promptBiometricAuth(
                            title = "Проверка биометрии",
                            subtitle = "Поднесите палец с сканеру для считывания",
                            negativeButtonText = "Отмена действия",
                            fragmentActivity = activity,
                            onSuccess = {
                                message = "Успех"
                                if (authenticate(idDoor)) {
                                    Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                                    val db = FirebaseFirestore.getInstance()

                                    Log.d(tag, "Successful authentication using biometrics")
                                    val taskData = SaveUserData(userMail.toString(), context)
                                    db.collection("access_history")
                                        .add(taskData)
                                        .addOnSuccessListener {
                                            Log.d(tag, "Successfully added")
                                        }
                                        .addOnFailureListener {
                                            Log.d(tag, "Encountered error while adding to db")
                                        }

                                } else {
                                    Toast.makeText(context, "Ошибка ввода", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            onFailed = {
                                message = "Неверный отпечаток"
                            },
                            onError = { _, error ->
                                message = error
                            }
                        )

                    },
                ) {
                    Text("Подтвердить")

                }
                Spacer(Modifier.height(com.example.acs.login.itemSpacing))
                Row {
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                    Button(
                        modifier = Modifier
                            .padding(),
                        enabled = true,
                        onClick = {
                            val editor = sharedPreferences.edit()
                            editor.putString("user_uid", null)
                            editor.putString("user_mail", null)
                            editor.apply()
                            onLoginClick()
                        },
                    ) {
                        Text("Logout")
                    }
                }
            }
        })
}



fun authenticate(username: String): Boolean{
    val idDoor = "404"
    return username == idDoor

}

@Preview(showSystemUi = true)
@Composable
fun PrevSignUp(){
    ACSTheme {
        Route.SignUpScreen()
    }
}