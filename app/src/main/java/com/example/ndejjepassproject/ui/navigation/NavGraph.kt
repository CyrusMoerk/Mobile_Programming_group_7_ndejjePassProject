package com.example.ndejjepassproject.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ndejjepassproject.data.db.ClearanceDatabase
import com.example.ndejjepassproject.ui.screens.AdminScreen
import com.example.ndejjepassproject.ui.screens.DashboardScreen
import com.example.ndejjepassproject.ui.screens.LoginScreen
import com.example.ndejjepassproject.ui.screens.PaymentScreen
import com.example.ndejjepassproject.viewmodel.AdminViewModel
import com.example.ndejjepassproject.viewmodel.AppViewModelFactory
import com.example.ndejjepassproject.viewmodel.AuthViewModel
import com.example.ndejjepassproject.viewmodel.DashboardViewModel
import com.example.ndejjepassproject.viewmodel.PaymentViewModel

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Setup : Screen("setup")
    data object Dashboard : Screen("dashboard")
    data object Payment : Screen("payment")
    data object Admin : Screen("admin")
    data object Scanner : Screen("scanner")
}

@Composable
fun AppNavGraph(db: ClearanceDatabase, navController: NavHostController = rememberNavController()) {
    val authVm: AuthViewModel = viewModel(factory = AppViewModelFactory(db))
    val authState by authVm.state.collectAsState()
    val studentId = authState.student?.id

    LaunchedEffect(authState.isLoggedIn, authState.needsSetup, authState.student?.role) {
        if (authState.isLoggedIn) {
            val dest = when {
                authState.needsSetup -> Screen.Setup.route
                authState.student?.role == "admin" -> Screen.Admin.route
                authState.student?.role == "security" -> Screen.Scanner.route
                else -> Screen.Dashboard.route
            }
            navController.navigate(dest) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(authVm, navController) }
        composable(Screen.Dashboard.route) {
            if (studentId == null) return@composable
            val vm: DashboardViewModel = viewModel(factory = AppViewModelFactory(db, studentId))
            DashboardScreen(vm, navController)
        }
        composable(Screen.Payment.route) {
            if (studentId == null) return@composable
            val vm: PaymentViewModel = viewModel(factory = AppViewModelFactory(db, studentId))
            PaymentScreen(vm, navController)
        }
        composable(Screen.Admin.route) {
            val vm: AdminViewModel = viewModel(factory = AppViewModelFactory(db))
            AdminScreen(vm)
        }
        composable(Screen.Setup.route) { Text("Academic setup coming soon") }
        composable(Screen.Scanner.route) { Text("Security scanner coming soon") }
    }
}