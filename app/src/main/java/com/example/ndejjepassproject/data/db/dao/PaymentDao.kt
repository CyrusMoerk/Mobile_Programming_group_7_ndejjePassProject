package com.example.ndejjepassproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import kotlinx.coroutines.flow.Flow
import com.example.ndejjepassproject.data.db.relations.PaymentWithReceipt

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Query("SELECT * FROM payments WHERE studentId = :studentId ORDER BY submittedAt DESC")
    fun getPaymentsByStudent(studentId: Int): Flow<List<PaymentEntity>>

    @Query("SELECT SUM(amount) FROM payments WHERE studentId = :studentId AND status = 'approved'")
    fun getTotalApproved(studentId: Int): Flow<Double?>

    @Query("SELECT * FROM payments WHERE status = 'pending' ORDER BY submittedAt DESC")
    fun getPendingPayments(): Flow<List<PaymentEntity>>

    @Query("UPDATE payments SET status = :status, rejectionReason = :reason WHERE id = :id")
    suspend fun updatePaymentStatus(id: Int, status: String, reason: String? = null)

    @Query("""
    SELECT payments.*, receipts.filePath, receipts.fileType 
    FROM payments 
    LEFT JOIN receipts 
    ON payments.id = receipts.paymentId
    WHERE payments.status = 'pending'
    ORDER BY payments.submittedAt DESC
""")
    fun getPendingPaymentsWithReceipts(): Flow<List<PaymentWithReceipt>>

}