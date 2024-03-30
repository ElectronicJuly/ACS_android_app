package com.example.acs.login

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.google.firebase.auth.auth

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@Composable
fun LoginScreen(onSignUpClick: () -> Unit){

    val (userName, setUsername) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, onPasswordChance) = rememberSaveable {
        mutableStateOf("")
    }

    val (checked, onCheckedChance) = rememberSaveable {
        mutableStateOf(false)
    }

    val isFieldsEmpty = userName.isNotEmpty() && password.isNotEmpty()
    val context = LocalContext.current.applicationContext

    val auth = Firebase.auth
    val currentUser = auth.currentUser

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        HeaderText(
            text = "Аутентификация",
            modifier = Modifier
                .padding(vertical = defaultPadding)

        )
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

        Spacer(Modifier.height(itemSpacing))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsEmpty,
            onClick = {
                auth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            Toast.makeText(
                                context,
                                "Аутентификация прошла успешно",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val user = auth.currentUser
                            onSignUpClick()
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                context,
                                "Вход не осуществлен",
                                Toast.LENGTH_SHORT,
                            ).show()}
            }}

        ) {
            Text("Вход")
        }


    }

    }


@Preview (showSystemUi = true)
@Composable
fun PrevLoginScreen(){
    ACSTheme {
        LoginScreen({})
    }
}

