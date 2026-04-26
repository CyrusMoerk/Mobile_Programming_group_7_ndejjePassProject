package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.data.repository.PaymentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
        viewModelScope.launch { repo.approvePayment(paymentId, studentId) }
    }

    fun reject(paymentId: Int, reason: String) {
        viewModelScope.launch { repo.rejectPayment(paymentId, reason) }
    }
}