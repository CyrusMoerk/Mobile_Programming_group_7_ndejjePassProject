// ui/navigation/NavGraph.kt
// Single source of truth for all navigation routes.
// Role-based routing: after login, user goes to their correct screen.

sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Setup    : Screen("setup")
    object Dashboard: Screen("dashboard")
    object Payment  : Screen("payment")
    object Admin    : Screen("admin")
    object Scanner  : Screen("scanner")
}

@Composable
fun AppNavGraph(db: ClearanceDatabase) {
    val navController = rememberNavController()
    val authVm: AuthViewModel = viewModel(factory = AppViewModelFactory(db))
    val authState by authVm.state.collectAsStateWithLifecycle()

    // React to login success — route based on role and setup status
    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) {
            val dest = when {
                authState.needsSetup                    -> Screen.Setup.route
                authState.student?.role == "admin"    -> Screen.Admin.route
                authState.student?.role == "security" -> Screen.Scanner.route
                else                                   -> Screen.Dashboard.route
            }
            navController.navigate(dest) { popUpTo(Screen.Login.route) { inclusive = true } }
        }
    }

    NavHost(navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route)     { LoginScreen(authVm, navController) }
        composable(Screen.Dashboard.route) {
            val vm: DashboardViewModel = viewModel(
                factory = AppViewModelFactory(db, authState.student!!.id)
            )
            DashboardScreen(vm, navController)
        }
        composable(Screen.Payment.route)  {
            val vm: PaymentViewModel = viewModel(
                factory = AppViewModelFactory(db, authState.student!!.id)
            )
            PaymentScreen(vm, navController)
        }
        composable(Screen.Admin.route)    {
            val vm: AdminViewModel = viewModel(factory = AppViewModelFactory(db))
            AdminScreen(vm)
        }
    }
}