// viewmodel/AdminViewModel.kt
// Admin-side ViewModel — sees all pending payments.
// approvePayment and rejectPayment delegate to PaymentRepository
// which recalculates clearance automatically.

data class AdminUiState(
    val pendingPayments: List<PaymentEntity> = emptyList(),
    val isLoading: Boolean = true,
    val actionMessage: String? = null
)

class AdminViewModel(private val repo: PaymentRepository) : ViewModel() {

    val state: StateFlow<AdminUiState> = repo.getPendingPayments()
        .map { AdminUiState(pendingPayments = it, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdminUiState())

    fun approve(paymentId: Int, studentId: Int) {
        viewModelScope.launch {
            repo.approvePayment(paymentId, studentId)
            // clearance is recalculated inside approvePayment
        }
    }

    fun reject(paymentId: Int, reason: String) {
        viewModelScope.launch {
            repo.rejectPayment(paymentId, reason)
        }
    }
}