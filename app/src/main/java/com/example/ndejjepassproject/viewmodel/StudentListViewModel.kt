package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.data.repository.StudentRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class StudentListUiState(
    val students: List<StudentEntity> = emptyList(),
    val isLoading: Boolean = true
)

class StudentListViewModel(private val repo: StudentRepository) : ViewModel() {

    val state: StateFlow<StudentListUiState> = repo.getAllStudents()
        .map { StudentListUiState(students = it, isLoading = false) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StudentListUiState()
        )
}