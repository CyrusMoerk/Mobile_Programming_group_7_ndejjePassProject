// data/repository/PaymentRepository.kt
// Handles payment submission and the clearance recalculation.
// recalculateClearance() is the core business rule —
// clearance % = (total approved payments ÷ 1,800,000) × 100

class PaymentRepository(
    private val paymentDao: PaymentDao,
    private val clearanceDao: ClearanceDao
) {
    // Student submits a payment — saved as "pending" until admin reviews
    suspend fun submitPayment(
        studentId: Int, amount: Double,
        reference: String, method: String
    ): Result<Unit> {
        return try {
            paymentDao.insertPayment(
                PaymentEntity(
                    studentId = studentId, amount = amount,
                    reference = reference, method = method
                )
            )
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    fun getStudentPayments(id: Int): Flow<List<PaymentEntity>> =
        paymentDao.getPaymentsByStudent(id)

    fun getPendingPayments(): Flow<List<PaymentEntity>> =
        paymentDao.getPendingPayments()

    // Admin approves → set status + recalculate clearance immediately
    suspend fun approvePayment(paymentId: Int, studentId: Int) {
        paymentDao.updatePaymentStatus(paymentId, "approved")
        recalculateClearance(studentId)
    }

    suspend fun rejectPayment(paymentId: Int, reason: String) {
        paymentDao.updatePaymentStatus(paymentId, "rejected", reason)
        // No clearance change on rejection
    }

    // THE CORE FORMULA:
    // clearance % = (sum of approved payments ÷ 1,800,000) × 100
    // Runs every time admin approves a payment
    private suspend fun recalculateClearance(studentId: Int) {
        val totalPaid = paymentDao.getTotalApproved(studentId).first() ?: 0.0
        val due = 1_800_000.0
        val pct = ((totalPaid / due) * 100).toInt().coerceAtMost(100)
        clearanceDao.updateProgress(studentId, totalPaid, pct, isCleared = pct >= 100)
    }
}