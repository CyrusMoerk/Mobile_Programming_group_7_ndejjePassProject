package com.example.ndejjepassproject.data.repository

import com.example.ndejjepassproject.data.db.dao.ClearanceDao
import com.example.ndejjepassproject.data.db.dao.PaymentDao
import com.example.ndejjepassproject.data.db.dao.ReceiptDao
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.data.db.entities.ReceiptEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PaymentRepository(
    private val paymentDao: PaymentDao,
    private val clearanceDao: ClearanceDao,
    private val receiptDao: ReceiptDao
) {

    suspend fun submitPayment(
        studentId: Int,
        amount: Double,
        method: String,
        filePath: String,
        fileType: String
    ): Result<Unit> {
        return try {
            val payment = PaymentEntity(
                studentId = studentId,
                amount    = amount,
                reference = "PENDING_APPROVAL",
                method    = method
            )
            val paymentId = paymentDao.insertPayment(payment)
            receiptDao.insertReceipt(
                ReceiptEntity(
                    studentId = studentId,
                    paymentId = paymentId.toInt(),
                    filePath  = filePath,
                    fileType  = fileType,
                    amount    = amount,
                    method    = method,
                    status    = "pending"
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getStudentPayments(id: Int): Flow<List<PaymentEntity>> =
        paymentDao.getPaymentsByStudent(id)

    fun getPendingReceipts(): Flow<List<ReceiptEntity>> =
        receiptDao.getPendingReceipts()
    fun getPendingPaymentsWithReceipts() =
        paymentDao.getPendingPaymentsWithReceipts()

    fun getReceiptsForStudent(studentId: Int): Flow<List<ReceiptEntity>> =
        receiptDao.getReceiptsForStudent(studentId)

    suspend fun approveReceipt(receiptId: Int, paymentId: Int, studentId: Int) {
        receiptDao.updateReceiptStatus(receiptId, "approved")
        paymentDao.updatePaymentStatus(paymentId, "approved")
        recalculateClearance(studentId)
    }

    suspend fun rejectReceipt(receiptId: Int, paymentId: Int) {
        receiptDao.updateReceiptStatus(receiptId, "rejected")
        paymentDao.updatePaymentStatus(paymentId, "rejected")
    }

    fun getPendingPayments(): Flow<List<PaymentEntity>> =
        paymentDao.getPendingPayments()

    suspend fun approvePayment(paymentId: Int, studentId: Int) {
        paymentDao.updatePaymentStatus(paymentId, "approved")
        recalculateClearance(studentId)
    }

    suspend fun rejectPayment(paymentId: Int, reason: String) {
        paymentDao.updatePaymentStatus(paymentId, "rejected", reason)
    }

    private suspend fun recalculateClearance(studentId: Int) {
        val totalPaid = paymentDao.getTotalApproved(studentId).first() ?: 0.0
        val due       = 1_800_000.0
        val pct       = ((totalPaid / due) * 100).toInt().coerceAtMost(100)
        val existing  = clearanceDao.getClearanceByStudent(studentId).first()
        if (existing == null) {
            clearanceDao.insertClearance(
                ClearanceEntity(
                    studentId   = studentId,
                    tuitionPaid = totalPaid,
                    percentage  = pct,
                    isCleared   = pct >= 100
                )
            )
        } else {
            clearanceDao.updateProgress(studentId, totalPaid, pct, pct >= 100)
        }
    }
}