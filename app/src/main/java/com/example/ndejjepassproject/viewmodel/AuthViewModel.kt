package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val student: StudentEntity? = null,
    val isLoggedIn: Boolean = false,
    val needsSetup: Boolean = false
)

class AuthViewModel(private val repo: StudentRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Please fill in all fields") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repo.login(email, password).fold(
                onSuccess = { student ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            student = student,
                            isLoggedIn = true,
                            needsSetup = !student.isSetupComplete
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
            )
        }
    }

    fun clearError() = _state.update { it.copy(error = null) }
}