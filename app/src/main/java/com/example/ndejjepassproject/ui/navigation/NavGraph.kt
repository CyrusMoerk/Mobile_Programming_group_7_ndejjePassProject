package com.example.ndejjepassproject.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ndejjepassproject.data.db.ClearanceDatabase
import com.example.ndejjepassproject.view.*
import com.example.ndejjepassproject.viewmodel.*

sealed class Screen(val route: String) {
    data object Login         : Screen("login")
    data object Setup         : Screen("setup")
    data object Dashboard     : Screen("dashboard")
    data object Payment       : Screen("payment")
    data object DeansReg      : Screen("deans_reg")
    data object ExamPermit    : Screen("exam_permit")
    data object QRCode        : Screen("qr_code")
    data object Profile       : Screen("profile")
    data object AdminDashboard: Screen("admin_dashboard")
    data object Admin         : Screen("admin")
    data object StudentList   : Screen("student_list")
    data object TuitionConfig : Screen("tuition_config")
    data object Security      : Screen("security")
}

@Composable
fun AppNavGraph(
    db: ClearanceDatabase,
    navController: NavHostController = rememberNavController()
) {
    val authVm: AuthViewModel = viewModel(factory = AppViewModelFactory(db))
    val authState by authVm.state.collectAsState()
    val studentId = authState.student?.id

    LaunchedEffect(authState.isLoggedIn, authState.needsSetup, authState.student?.role) {
        if (authState.isLoggedIn) {
            val dest = when {
                authState.needsSetup                    -> Screen.Setup.route
                authState.student?.role == "admin"      -> Screen.AdminDashboard.route
                authState.student?.role == "security"   -> Screen.Security.route
                else                                    -> Screen.Dashboard.route
            }
            navController.navigate(dest) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // ── Login ─────────────────────────────────────────────
        composable(Screen.Login.route) {
            val state by authVm.state.collectAsState()
            LoginScreen(
                onLoginClick = { _, email, password -> authVm.login(email, password) },
                errorMessage = state.error
            )
        }

        // ── Student Dashboard ─────────────────────────────────
        composable(Screen.Dashboard.route) {
            if (studentId == null) return@composable
            val vm: DashboardViewModel =
                viewModel(factory = AppViewModelFactory(db, studentId))
            val state by vm.state.collectAsState()
            StudentDashboardScreen(
                studentName  = state.student?.name ?: "",
                regNumber    = state.student?.regNumber ?: "",
                programme    = "${state.student?.programName} — Year ${state.student?.year}, Sem ${state.student?.semester}",
                tuitionPaid  = state.clearance?.tuitionPaid ?: 0.0,
                totalTuition = state.clearance?.tuitionDue ?: 1_800_000.0,
                nchePaid     = true,
                onMakePayment = { navController.navigate(Screen.Payment.route) },
                onViewCourses = { navController.navigate(Screen.DeansReg.route) },
                onViewPermit  = { navController.navigate(Screen.ExamPermit.route) },
                onViewQRCode = { navController.navigate(Screen.QRCode.route) },
                onProfile     = { navController.navigate(Screen.Profile.route) },
                onLogout      = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Payment Submission ────────────────────────────────
        composable(Screen.Payment.route) {
            if (studentId == null) return@composable
            val vm: PaymentViewModel =
                viewModel(factory = AppViewModelFactory(db, studentId))
            PaymentSubmissionScreen(
                balanceDue = 1_800_000.0,
                onSubmit = { method, amount, filePath, fileType ->
                    vm.onMethodSelected(
                        when (method) {
                            "MTN Mobile Money" -> "mtn"
                            "Airtel Money"     -> "airtel"
                            else               -> "bank"
                        }
                    )
                    vm.onAmountChanged(amount)
                    vm.submitPaymentWithReceipt(filePath, fileType)
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Dean's Reg ────────────────────────────────────────
        composable(Screen.DeansReg.route) {
            if (studentId == null) return@composable
            val vm: DashboardViewModel =
                viewModel(factory = AppViewModelFactory(db, studentId))
            val state by vm.state.collectAsState()
            DeansRegScreen(
                semester = "Semester ${state.student?.semester ?: 1} — ${state.student?.year ?: 1} Year",
                onSubmit = { selected, retakes ->
                    vm.saveCourses(studentId, selected, retakes)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Exam Permit ───────────────────────────────────────
        composable(Screen.ExamPermit.route) {
            if (studentId == null) return@composable
            val vm: DashboardViewModel =
                viewModel(factory = AppViewModelFactory(db, studentId))
            val state by vm.state.collectAsState()
            ExamPermitScreen(
                studentName    = state.student?.name ?: "",
                regNumber      = state.student?.regNumber ?: "",
                programme      = state.student?.programName ?: "",
                hall           = state.student?.hall ?: "",
                nationality    = state.student?.nationality ?: "Ugandan",
                studyMode      = state.student?.studyMode ?: "Day",
                semester       = state.student?.semester?.toString() ?: "1",
                academicYear   = "2025/2026",
                isFullyCleared = state.clearance?.isCleared == true,
                courses        = state.courseUnits.filter { !it.isRetake }.map {
                    CourseItem(it.courseCode, it.courseName, it.creditUnits)
                },
                retakes        = state.courseUnits.filter { it.isRetake }.map {
                    CourseItem(it.courseCode, it.courseName, it.creditUnits, true)
                },
                onBack  = { navController.popBackStack() },
                onPrint = { }
            )
        }

        // ── QR Code ───────────────────────────────────────────
        composable(Screen.QRCode.route) {
            if (studentId == null) return@composable
            val vm: DashboardViewModel =
                viewModel(factory = AppViewModelFactory(db, studentId))
            val state by vm.state.collectAsState()
            QRCodeScreen(
                studentName     = state.student?.name ?: "",
                regNumber       = state.student?.regNumber ?: "",
                programme       = state.student?.programName ?: "",
                clearanceStatus = "${state.clearance?.percentage ?: 0}% — ${
                    if (state.clearance?.isCleared == true) "Cleared" else "Partial"
                }",
                percentage      = state.clearance?.percentage ?: 0,
                isCleared       = state.clearance?.isCleared == true,
                onBack          = { navController.popBackStack() }
            )
        }

        // ── Profile ───────────────────────────────────────────
        composable(Screen.Profile.route) {
            if (studentId == null) return@composable
            val vm: ProfileViewModel =
                viewModel(factory = AppViewModelFactory(db, studentId))
            val student   by vm.studentState.collectAsState()
            val editState by vm.editState.collectAsState()
            val pwState   by vm.pwState.collectAsState()
            ProfileScreen(
                studentName         = student?.name ?: "",
                regNumber           = student?.regNumber ?: "",
                programme           = student?.programName ?: "",
                email               = student?.email ?: "",
                photoPath           = student?.photoPath ?: "",
                editState           = editState,
                pwState             = pwState,
                onNameChanged       = vm::onNameChanged,
                onYearChanged       = vm::onYearChanged,
                onSemesterChanged   = vm::onSemesterChanged,
                onHallChanged       = vm::onHallChanged,
                onNationalityChanged= vm::onNationalityChanged,
                onStudyModeChanged  = vm::onStudyModeChanged,
                onIntakeChanged     = vm::onIntakeChanged,
                onSaveProfile       = vm::saveProfile,
                onPhotoSelected     = vm::updatePhoto,
                onCurrentPwChanged  = vm::onCurrentPwChanged,
                onNewPwChanged      = vm::onNewPwChanged,
                onConfirmPwChanged  = vm::onConfirmPwChanged,
                onChangePassword    = vm::changePassword,
                onBack              = { navController.popBackStack() },
                onLogout            = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Admin Dashboard ───────────────────────────────────
        composable(Screen.AdminDashboard.route) {
            val adminVm: AdminViewModel = viewModel(factory = AppViewModelFactory(db))
            val studentVm: StudentListViewModel = viewModel(factory = AppViewModelFactory(db))
            val adminState by adminVm.state.collectAsState()
            val studentState by studentVm.state.collectAsState()
            AdminDashboardScreen(
                pendingCount = adminState.payments.count { it.status == "pending" },
                totalStudents  = studentState.students.size,
                onApprovePayments = { navController.navigate(Screen.Admin.route) },
                onViewStudents    = { navController.navigate(Screen.StudentList.route) },
                onTuitionConfig   = { navController.navigate(Screen.TuitionConfig.route) },
                onLogout          = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Admin — Payment Approval ──────────────────────────
        composable(Screen.Admin.route) {
            val vm: AdminViewModel = viewModel(factory = AppViewModelFactory(db))
            val state by vm.state.collectAsState()
            PaymentApprovalScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        // ── Student List ──────────────────────────────────────
        composable(Screen.StudentList.route) {
            val vm: StudentListViewModel =
                viewModel(factory = AppViewModelFactory(db))
            val state by vm.state.collectAsState()
            StudentListScreen(
                students = state.students.map { s ->
                    StudentSummary(
                        id              = s.id.toString(),
                        name            = s.name,
                        regNumber       = s.regNumber,
                        programme       = s.programName,
                        percentage      = 0, // wire clearance later
                        nchePaid        = true,
                        permitGenerated = false
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Tuition Config ────────────────────────────────────
        composable(Screen.TuitionConfig.route) {
            TuitionConfigScreen(
                onBack = { navController.popBackStack() },
                onSave = { _, _, _, _, _ -> navController.popBackStack() }
            )
        }

        // ── Security Scanner ──────────────────────────────────
        composable(Screen.Security.route) {
            SecurityScannerScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Setup ─────────────────────────────────────────────
        composable(Screen.Setup.route) {
            androidx.compose.material3.Text("Academic setup coming soon")
        }
    }
}