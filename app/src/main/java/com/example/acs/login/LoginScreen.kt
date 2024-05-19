package com.example.acs.login


import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.acs.components.HeaderText
import com.example.acs.components.LoginTextField
import com.example.acs.components.PasswordTextField
import com.example.acs.ui.theme.ACSTheme
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant

val defaultPadding = 16.dp
val itemSpacing = 8.dp

fun SaveUserData(auth: String, context: Context): HashMap<String, Any> {
    val taskData = HashMap<String, Any>()
    val instant = Instant.now()
    val timestamp = instant.toEpochMilli()
    taskData["user"] = auth
    taskData["date"] = Timestamp.now()

    taskData["device_model"] = Build.MODEL
    taskData["device_product"] = Build.PRODUCT
    taskData["api_level"] = Build.VERSION.SDK_INT

    taskData["devide_id"] = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    taskData["android_version"] = System.getProperty("os.version")

    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    taskData["version_name"] = packageInfo.versionName
    return taskData
}


@Composable
fun LoginScreen(onSignUpClick: () -> Unit){

    val openDrawer = remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)


    val (userName, setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, onPasswordChance) = rememberSaveable {
        mutableStateOf("")
    }



    val isFieldsEmpty = userName.isNotEmpty() && password.isNotEmpty()
    val context = LocalContext.current.applicationContext

    val auth = Firebase.auth
    val currentUser = auth.currentUser
    val tag = "MyActivity"

    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_uid", null)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topEnd = 16.dp))
                        .width(250.dp)
                        .background(color = Color.Black.copy(alpha = 0.8f))
                ) {
                    // Your sidebar content goes here
                    Text("This is the sidebar content")
                }
            },
            content = {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(defaultPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
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
                        text = "Аутентификация",
                        modifier = Modifier
                            .padding(vertical = defaultPadding)

                    )

                    LaunchedEffect(openDrawer.value) {
                        if (openDrawer.value) {
                            drawerState.open()
                            openDrawer.value = false // Reset state after opening
                        }
                    }
                    LoginTextField(

                        value = userName,
                        onValueChange = setUsername,
                        labelText = "Идентификатор",
                        leadingIcon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth()

                    )
                    Spacer(Modifier.height(itemSpacing))

                    PasswordTextField(
                        value = password,
                        onValueChange = onPasswordChance,
                        labelText = "Пароль",
                        leadingIcon = Icons.Default.Lock,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation()

                    )
                    Spacer(Modifier.height(itemSpacing))

                    val isChecked = remember { mutableStateOf(false) }
                    val checkboxRow = Row(
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        val checkbox = Checkbox(
                            checked = isChecked.value, // Set initial checked state

                            enabled = true,
                            onCheckedChange = { isChecked.value = it }

                        )
                        val text = Text(
                            text = "Запомни меня",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isFieldsEmpty,
                        onClick = {

                            auth.signInWithEmailAndPassword(userName, password)
                                .addOnCompleteListener() { task ->
                                    if (task.isSuccessful) {
                                        val db = FirebaseFirestore.getInstance()
                                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                                        Toast.makeText(
                                            context,
                                            "Аутентификация прошла успешно",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                        db.collection("login_history")
                                            .add(SaveUserData(auth.currentUser?.email.toString(), context))
                                            .addOnSuccessListener {
                                                Log.d(tag, "Successfully added")
                                            }
                                            .addOnFailureListener {
                                                Log.d(tag, "Encountered error while adding to db")
                                            }
                                        if (isChecked.value) {
                                            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                            val editor = sharedPreferences.edit()
                                            editor.putString("user_uid", auth.currentUser?.uid.toString())
                                            editor.putString("user_mail", auth.currentUser?.email.toString())
                                            editor.apply()
                                        }
                                        onSignUpClick()

                                    } else {
                                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                        Toast.makeText(
                                            context,
                                            "Вход не осуществлен",
                                            Toast.LENGTH_SHORT,
                                        ).show()}
                                }
                        }

                    ) {
                        Text("Вход")
                    }
                }
            },
            gesturesEnabled = true, // Allow swiping to open/close drawer
        )
    }



@Preview (showSystemUi = true)
@Composable
fun PrevLoginScreen(){
    ACSTheme {
        LoginScreen({})
    }
}

