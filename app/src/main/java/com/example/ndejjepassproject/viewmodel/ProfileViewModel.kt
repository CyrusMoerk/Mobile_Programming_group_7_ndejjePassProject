// viewmodel/ProfileViewModel.kt
// Exposes TWO separate state objects:
// 1. studentState — read-only display (all fields including locked ones for display)
// 2. editState    — writable, contains ONLY the 3 editable fields
//
// The UI binds input fields to editState only.
// Locked fields shown on screen come from studentState — read only, no TextField.

data class ProfileEditState(
    val name: String = "",
    val year: Int = 1,
    val semester: Int = 1,
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

    // Read-only display state — includes locked fields for showing on screen
    val studentState: StateFlow<StudentEntity?> =
        repo.getStudent(studentId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Writable edit state — ONLY contains the 3 fields student can change
    private val _editState = MutableStateFlow(ProfileEditState())
    val editState: StateFlow<ProfileEditState> = _editState.asStateFlow()

    private val _pwState = MutableStateFlow(PasswordState())
    val pwState: StateFlow<PasswordState> = _pwState.asStateFlow()

    // Pre-fill the edit form when the screen opens
    init {
        viewModelScope.launch {
            studentState.filterNotNull().first().let { s ->
                _editState.update { it.copy(name = s.name, year = s.year, semester = s.semester) }
            }
        }
    }

    // Editable field setters — only these 3 exist
    fun onNameChanged(v: String)     = _editState.update { it.copy(name = v) }
    fun onYearChanged(v: Int)        = _editState.update { it.copy(year = v) }
    fun onSemesterChanged(v: Int)    = _editState.update { it.copy(semester = v) }

    // No setter exists for email, regNumber, programCode, or role.
    // The ViewModel makes it structurally impossible for the UI to change them.

    fun saveProfile() {
        val s = _editState.value
        viewModelScope.launch {
            _editState.update { it.copy(isSaving = true, error = null) }
            repo.updateProfile(studentId, StudentUpdateRequest(s.name, s.year, s.semester))
                .fold(
                    onSuccess = { _editState.update { it.copy(isSaving = false, saveSuccess = true) } },
                    onFailure = { e -> _editState.update { it.copy(isSaving = false, error = e.message) } }
                )
        }
    }

    // Password change setters
    fun onCurrentPwChanged(v: String) = _pwState.update { it.copy(current = v) }
    fun onNewPwChanged(v: String)     = _pwState.update { it.copy(newPw = v) }
    fun onConfirmPwChanged(v: String) = _pwState.update { it.copy(confirm = v) }

    fun changePassword() {
        val p = _pwState.value
        viewModelScope.launch {
            _pwState.update { it.copy(isSaving = true, error = null) }
            repo.changePassword(studentId, PasswordChangeRequest(p.current, p.newPw, p.confirm))
                .fold(
                    onSuccess = { _pwState.update { it.copy(isSaving = false, saveSuccess = true, current = "", newPw = "", confirm = "") } },
                    onFailure = { e -> _pwState.update { it.copy(isSaving = false, error = e.message) } }
                )
        }
    }
}