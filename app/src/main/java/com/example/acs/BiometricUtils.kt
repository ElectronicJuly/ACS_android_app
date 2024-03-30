package com.example.acs

import android.bluetooth.le.AdvertiseSettings
import android.hardware.biometrics.BiometricManager
import android.os.Build
import androidx.annotation.RequiresApi

inline fun authenticators(aboveVersion9:() -> Int, belowVersion10:() -> Int) {
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
        aboveVersion9.invoke()
    } else{
        belowVersion10.invoke()
    }}



@RequiresApi(Build.VERSION_CODES.R)
inline fun BiometricManager.checkExistence(
    onSuccess:(Int) -> Unit,
    onError:(String)->Unit,
    openSettings: () -> Unit){

    val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK

    when(canAuthenticate(authenticators)){
        BiometricManager.BIOMETRIC_SUCCESS ->{
            onSuccess.invoke(authenticators)
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            onError.invoke("BIOMETRIC_ERROR_HW_UNAVAILABLE")
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            openSettings.invoke()
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            onError.invoke("BIOMETRIC_ERROR_NO_HARDWARE")
        }

        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            onError.invoke("BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED")
        }
    }


}
