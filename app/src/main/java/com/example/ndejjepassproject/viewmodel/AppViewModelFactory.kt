//viewmodel/AppViewModelFactory.kt
// ViewModelFactory manually creates ViewModels with constructor arguments.
// Needed because Room and repositories can't be injected automatically
// without Hilt/Dagger — this is the simple manual approach.

class AppViewModelFactory(
    private val db: ClearanceDatabase,
    private val studentId: Int = -1
) : ViewModelProvider.Factory {

    private val studentRepo by lazy { StudentRepository(db.studentDao(), db.clearanceDao()) }
    private val paymentRepo by lazy { PaymentRepository(db.paymentDao(), db.clearanceDao()) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AuthViewModel::class.java      -> AuthViewModel(studentRepo)
            DashboardViewModel::class.java -> DashboardViewModel(studentRepo, paymentRepo, studentId)
            PaymentViewModel::class.java   -> PaymentViewModel(paymentRepo, studentId)
            AdminViewModel::class.java    -> AdminViewModel(paymentRepo)
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}