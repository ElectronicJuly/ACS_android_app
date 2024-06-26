package com.example.acs.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.acs.components.HeaderText
import com.example.acs.components.LoginTextField
import com.example.acs.login.defaultPadding
import com.example.acs.login.itemSpacing
import com.example.acs.ui.theme.ACSTheme
import com.example.acs.BiometricAuthenticator
import com.example.acs.Route

@Composable
fun SignUpScreen()  {

    val (idDoor, onIdDoorChange) = rememberSaveable {
        mutableStateOf("")
    }

    val isFieldsNotEmpty = idDoor.isNotEmpty()
    val context = LocalContext.current.applicationContext
    val biometricAuthenticator = BiometricAuthenticator(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Доступ",
            modifier = Modifier
                .padding(vertical = defaultPadding)
        )
        LoginTextField(
            modifier = Modifier.fillMaxWidth(),
            value = idDoor,
            onValueChange = onIdDoorChange,
            labelText = "Номер",
            leadingIcon = Icons.Default.ExitToApp
        )
        Spacer(Modifier.height(itemSpacing + 10.dp))
        val activity = LocalContext.current as FragmentActivity
        var message by remember {
            mutableStateOf("")
        }
        Button(modifier = Modifier.fillMaxWidth(), enabled = isFieldsNotEmpty,
                onClick = {

                    biometricAuthenticator.promptBiometricAuth(
                        title = "Проверка биометрии",
                        subtitle = "Поднесите палец с сканеру для считывания",
                        negativeButtonText = "Отмена действия",
                        fragmentActivity = activity,
                        onSuccess = {
                            message = "Успех"
                            if (authenticate(idDoor)){
                                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()

                            } else {
                                Toast.makeText(context,"Ошибка ввода", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onFailed = {
                            message = "Неверный отпечаток"
                        },
                        onError = { _, error ->
                            message =error
                        }
                    )

        }, ) {
            Text("Подтвердить")
            
        }


    }

}




private fun authenticate(username: String): Boolean{
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