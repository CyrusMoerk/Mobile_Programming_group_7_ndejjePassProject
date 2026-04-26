// data/db/dao/PaymentDao.kt
// All SQL for the payments table.
// getTotalApproved drives the clearance % calculation.

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    // Student's own payment history — newest first
    @Query("SELECT * FROM payments WHERE studentId = :studentId ORDER BY submittedAt DESC")
    fun getPaymentsByStudent(studentId: Int): Flow<List<PaymentEntity>>

    // SUM of all APPROVED amounts — feeds into clearance % formula
    @Query("SELECT SUM(amount) FROM payments WHERE studentId = :studentId AND status = 'approved'")
    fun getTotalApproved(studentId: Int): Flow<Double?>

    // Admin sees all payments across all students
    @Query("SELECT * FROM payments WHERE status = 'pending' ORDER BY submittedAt DESC")
    fun getPendingPayments(): Flow<List<PaymentEntity>>

    @Query("UPDATE payments SET status = :status, rejectionReason = :reason WHERE id = :id")
    suspend fun updatePaymentStatus(id: Int, status: String, reason: String? = null)
}