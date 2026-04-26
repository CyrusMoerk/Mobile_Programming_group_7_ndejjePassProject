package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.data.repository.PaymentRepository
import com.example.ndejjepassproject.data.repository.StudentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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