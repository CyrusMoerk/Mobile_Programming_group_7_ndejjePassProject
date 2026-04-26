package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ndejjepassproject.data.db.ClearanceDatabase
import com.example.ndejjepassproject.data.repository.PaymentRepository
import com.example.ndejjepassproject.data.repository.StudentRepository

class AppViewModelFactory(
    private val db: ClearanceDatabase,
    private val studentId: Int = -1
) : ViewModelProvider.Factory {

    private val studentRepo by lazy { StudentRepository(db.studentDao(), db.clearanceDao()) }
    private val paymentRepo by lazy { PaymentRepository(db.paymentDao(), db.clearanceDao()) }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AuthViewModel::class.java -> AuthViewModel(studentRepo)
            DashboardViewModel::class.java -> DashboardViewModel(studentRepo, paymentRepo, studentId)
            PaymentViewModel::class.java -> PaymentViewModel(paymentRepo, studentId)
            AdminViewModel::class.java -> AdminViewModel(paymentRepo)
            ProfileViewModel::class.java -> ProfileViewModel(studentRepo, studentId)
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}