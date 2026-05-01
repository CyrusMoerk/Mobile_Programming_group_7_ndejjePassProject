package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.CourseUnitEntity
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.data.repository.PaymentRepository
import com.example.ndejjepassproject.data.repository.StudentRepository
import com.example.ndejjepassproject.view.CourseItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DashboardUiState(
    val student: StudentEntity? = null,
    val clearance: ClearanceEntity? = null,
    val payments: List<PaymentEntity> = emptyList(),
    val courseUnits: List<CourseUnitEntity> = emptyList(),
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
        paymentRepo.getStudentPayments(studentId),
        studentRepo.getCourseUnits(studentId)
    ) { student, clearance, payments, courses ->
        DashboardUiState(student, clearance, payments, courses, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun saveCourses(
        studentId: Int,
        selected: List<CourseItem>,
        retakes: List<CourseItem>
    ) {
        viewModelScope.launch {
            val entities = selected.map {
                CourseUnitEntity(
                    studentId   = studentId,
                    courseCode  = it.code,
                    courseName  = it.name,
                    creditUnits = it.creditUnits,
                    isRetake    = false
                )
            } + retakes.map {
                CourseUnitEntity(
                    studentId   = studentId,
                    courseCode  = it.code,
                    courseName  = it.name,
                    creditUnits = it.creditUnits,
                    isRetake    = true
                )
            }
            studentRepo.saveCourseUnits(studentId, entities)
        }
    }
}