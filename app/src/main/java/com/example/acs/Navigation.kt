package com.example.acs
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.acs.login.LoginScreen
import com.example.acs.signup.SignUpScreen
sealed class Route{
    data class LoginScreen(val name: String = "Login"): Route()
    data class SignUpScreen(val name: String = "Login"): Route()
}
@Composable
fun MyNavigation(navHostController: NavHostController){NavHost(
        navController = navHostController,
        startDestination =  "login_flow")
{
    navigation(startDestination = Route.LoginScreen().name, route = "login_flow") {
        composable(route = Route.LoginScreen().name) {
            LoginScreen(onSignUpClick = {
                navHostController.navigate(
                    "signUp"
                ) {
                    launchSingleTop = true
                    popUpTo(0)
                }
            })
        }
        composable("signUp") {
            SignUpScreen(onLoginClick = {
                navHostController.navigate(
                    "login"
                ) {
                    launchSingleTop = true
                    popUpTo(0)
                }
            })
        }
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