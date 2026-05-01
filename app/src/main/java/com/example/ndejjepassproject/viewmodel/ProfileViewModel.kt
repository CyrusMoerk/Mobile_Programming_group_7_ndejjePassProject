package com.example.ndejjepassproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.data.model.PasswordChangeRequest
import com.example.ndejjepassproject.data.model.StudentUpdateRequest
import com.example.ndejjepassproject.data.repository.StudentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileEditState(
    val name: String = "",
    val year: Int = 1,
    val semester: Int = 1,
    val hall: String = "",
    val nationality: String = "Ugandan",
    val studyMode: String = "Day",
    val intake: String = "August",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

data class PasswordState(
    val current: String = "",
    val newPw: String = "",
    val confirm: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val repo: StudentRepository,
    private val studentId: Int
) : ViewModel() {

    val studentState: StateFlow<StudentEntity?> =
        repo.getStudent(studentId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _editState = MutableStateFlow(ProfileEditState())
    val editState: StateFlow<ProfileEditState> = _editState.asStateFlow()

    private val _pwState = MutableStateFlow(PasswordState())
    val pwState: StateFlow<PasswordState> = _pwState.asStateFlow()

    init {
        viewModelScope.launch {
            studentState.filterNotNull().first().let { s ->
                _editState.update {
                    it.copy(
                        name        = s.name,
                        year        = s.year,
                        semester    = s.semester,
                        hall        = s.hall,
                        nationality = s.nationality,
                        studyMode   = s.studyMode,
                        intake      = s.intake
                    )
                }
            }
        }
    }

    fun onNameChanged(v: String)        = _editState.update { it.copy(name = v) }
    fun onYearChanged(v: Int)           = _editState.update { it.copy(year = v) }
    fun onSemesterChanged(v: Int)       = _editState.update { it.copy(semester = v) }
    fun onHallChanged(v: String)        = _editState.update { it.copy(hall = v) }
    fun onNationalityChanged(v: String) = _editState.update { it.copy(nationality = v) }
    fun onStudyModeChanged(v: String)   = _editState.update { it.copy(studyMode = v) }
    fun onIntakeChanged(v: String)      = _editState.update { it.copy(intake = v) }

    fun saveProfile() {
        val s = _editState.value
        viewModelScope.launch {
            _editState.update { it.copy(isSaving = true, error = null) }
            repo.updateProfile(
                studentId,
                StudentUpdateRequest(
                    name        = s.name,
                    year        = s.year,
                    semester    = s.semester,
                    hall        = s.hall,
                    nationality = s.nationality,
                    studyMode   = s.studyMode,
                    intake      = s.intake
                )
            ).fold(
                onSuccess = { _editState.update { it.copy(isSaving = false, saveSuccess = true) } },
                onFailure = { e -> _editState.update { it.copy(isSaving = false, error = e.message) } }
            )
        }
    }

    fun updatePhoto(path: String) {
        viewModelScope.launch { repo.updatePhotoPath(studentId, path) }
    }

    fun onCurrentPwChanged(v: String) = _pwState.update { it.copy(current = v) }
    fun onNewPwChanged(v: String)     = _pwState.update { it.copy(newPw = v) }
    fun onConfirmPwChanged(v: String) = _pwState.update { it.copy(confirm = v) }

    fun changePassword() {
        val p = _pwState.value
        viewModelScope.launch {
            _pwState.update { it.copy(isSaving = true, error = null) }
            repo.changePassword(
                studentId,
                PasswordChangeRequest(p.current, p.newPw, p.confirm)
            ).fold(
                onSuccess = {
                    _pwState.update {
                        it.copy(
                            isSaving = false, saveSuccess = true,
                            current = "", newPw = "", confirm = ""
                        )
                    }
                },
                onFailure = { e ->
                    _pwState.update { it.copy(isSaving = false, error = e.message) }
                }
            )
        }
    }
}