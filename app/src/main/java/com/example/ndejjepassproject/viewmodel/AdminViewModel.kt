package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.relations.PaymentWithReceipt
import com.example.ndejjepassproject.data.repository.PaymentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AdminUiState(
    val payments: List<PaymentWithReceipt> = emptyList(),
    val isLoading: Boolean = true
)

class AdminViewModel(private val repo: PaymentRepository) : ViewModel() {

    val state: StateFlow<AdminUiState> =
        repo.getPendingPaymentsWithReceipts()
            .map { AdminUiState(payments = it, isLoading = false) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                AdminUiState()
            )

    fun approve(receiptId: Int, paymentId: Int, studentId: Int) {
        viewModelScope.launch {
            repo.approveReceipt(receiptId, paymentId, studentId)
        }
    }

    fun reject(receiptId: Int, paymentId: Int) {
        viewModelScope.launch {
            repo.rejectReceipt(receiptId, paymentId)
        }
    }
}