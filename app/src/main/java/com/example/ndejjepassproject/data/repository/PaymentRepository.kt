package com.example.ndejjepassproject.data.repository

import com.example.ndejjepassproject.data.db.dao.ClearanceDao
import com.example.ndejjepassproject.data.db.dao.PaymentDao
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PaymentRepository(
    private val paymentDao: PaymentDao,
    private val clearanceDao: ClearanceDao
) {
    suspend fun submitPayment(
        studentId: Int,
        amount: Double,
        reference: String,
        method: String
    ): Result<Unit> {
        return try {
            paymentDao.insertPayment(
                PaymentEntity(
                    studentId = studentId,
                    amount = amount,
                    reference = reference,
                    method = method
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getStudentPayments(id: Int): Flow<List<PaymentEntity>> = paymentDao.getPaymentsByStudent(id)

    fun getPendingPayments(): Flow<List<PaymentEntity>> = paymentDao.getPendingPayments()

    suspend fun approvePayment(paymentId: Int, studentId: Int) {
        paymentDao.updatePaymentStatus(paymentId, "approved")
        recalculateClearance(studentId)
    }

    suspend fun rejectPayment(paymentId: Int, reason: String) {
        paymentDao.updatePaymentStatus(paymentId, "rejected", reason)
    }

    private suspend fun recalculateClearance(studentId: Int) {
        val totalPaid = paymentDao.getTotalApproved(studentId).first() ?: 0.0
        val due = 1_800_000.0
        val pct = ((totalPaid / due) * 100).toInt().coerceAtMost(100)
        val existing = clearanceDao.getClearanceByStudent(studentId).first()

        if (existing == null) {
            clearanceDao.insertClearance(
                ClearanceEntity(
                    studentId = studentId,
                    tuitionPaid = totalPaid,
                    percentage = pct,
                    isCleared = pct >= 100
                )
            )
        } else {
            clearanceDao.updateProgress(studentId, totalPaid, pct, cleared = pct >= 100)
        }
    }
}