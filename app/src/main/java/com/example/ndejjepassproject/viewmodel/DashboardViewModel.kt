/ viewmodel/DashboardViewModel.kt
// combine() merges three separate Flows into one DashboardUiState.
// Any change in student, clearance, or payments triggers a UI rebuild.

data class DashboardUiState(
    val student: StudentEntity? = null,
    val clearance: ClearanceEntity? = null,
    val payments: List<PaymentEntity> = emptyList(),
    val isLoading: Boolean = true
)

class DashboardViewModel(
    private val studentRepo: StudentRepository,
    private val paymentRepo: PaymentRepository,
    private val studentId: Int
) : ViewModel() {

    val state: StateFlow<DashboardUiState> = combine(
        studentRepo.getStudent(studentId),
        studentRepo.getClearance(studentId),
        paymentRepo.getStudentPayments(studentId)
    ) { student, clearance, payments ->
        DashboardUiState(student, clearance, payments, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}