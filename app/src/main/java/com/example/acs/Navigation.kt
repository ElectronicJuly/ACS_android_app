package com.example.acs
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.acs.history.HistoryScreen
import com.example.acs.login.LoginScreen
import com.example.acs.signup.SignUpScreen

sealed class Route{
    data class LoginScreen(val name: String = "Login"): Route()
    data class SignUpScreen(val name: String = "Login"): Route()

    data class HistoryScreen(val name: String = "Login"): Route()
}
@Composable
fun MyNavigation(navHostController: NavHostController){

    var startScreen = Route.LoginScreen().name
    val context = LocalContext.current.applicationContext
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_uid", null)
    if (userId != null)
    {
        startScreen = "signup"
    }


    NavHost(
        navController = navHostController,
        startDestination =  "login_flow")
{
    navigation(startDestination = startScreen, route = "login_flow") {
        composable(route = Route.LoginScreen().name) {
            LoginScreen(onSignUpClick = {
                navHostController.navigate("signUp") {
                    launchSingleTop = true
                    popUpTo(0)
                }
            })
        }
        composable("signUp") {
            SignUpScreen(onLoginClick = {
                navHostController.navigate("login") {
                    launchSingleTop = true
                    popUpTo(0)
                }
            }, onHistoryClick = {
                navHostController.navigate(
                    "history"
                ) {
                    launchSingleTop = true
                    popUpTo(0)
                }
            })
        }
        composable("history"){HistoryScreen(onAccessClick = {
            navHostController.navigate("signup") {
                launchSingleTop = true
                popUpTo(0)
            }
        }
        )}

    };
}
}

fun NavController.navigateToSingleTop(route:String){
    navigate(route){
        popUpTo(graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    } }