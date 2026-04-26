package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.repository.PaymentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentUiState(
    val method: String = "mtn",
    val phone: String = "",
    val amount: String = "",
    val reference: String = "",
    val isProcessing: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class PaymentViewModel(
    private val repo: PaymentRepository,
    private val studentId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentUiState())
    val state: StateFlow<PaymentUiState> = _state.asStateFlow()

    fun onMethodSelected(m: String) = _state.update { it.copy(method = m) }
    fun onPhoneChanged(v: String) = _state.update { it.copy(phone = v) }
    fun onAmountChanged(v: String) = _state.update { it.copy(amount = v) }
    fun onRefChanged(v: String) = _state.update { it.copy(reference = v) }

    fun submitPayment() {
        val s = _state.value
        val amt = s.amount.toDoubleOrNull()
        when {
            amt == null || amt <= 0 -> {
                _state.update { it.copy(error = "Enter a valid amount") }
                return
            }
            s.method != "bank" && s.phone.isBlank() -> {
                _state.update { it.copy(error = "Enter your phone number") }
                return
            }
            s.method == "bank" && s.reference.isBlank() -> {
                _state.update { it.copy(error = "Enter the bank reference number") }
                return
            }
        }

        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }
            if (s.method != "bank") delay(3000L)
            val ref = if (s.method == "bank") s.reference else "MM-${System.currentTimeMillis()}"
            repo.submitPayment(studentId, amt, ref, s.method).fold(
                onSuccess = { _state.update { it.copy(isProcessing = false, isSuccess = true) } },
                onFailure = { e -> _state.update { it.copy(isProcessing = false, error = e.message) } }
            )
        }
    }

    fun clearError() = _state.update { it.copy(error = null) }

    fun reset() {
        _state.value = PaymentUiState()
    }
}