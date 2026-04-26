// data/model/StudentUpdateRequest.kt
// This is the ONLY object the student's edit form ever sees.
// It contains ONLY the 4 editable fields.
// The ViewModel takes this in, the repository takes this in.
// Locked fields (email, regNumber, programCode, role) are never in here.

data class StudentUpdateRequest(
    val name: String,       // student can update their display name
    val year: Int,          // changes each academic year
    val semester: Int      // changes each semester
)

// Separate request for password change — kept intentionally isolated
// from the profile update so they are two distinct user actions.
data class PasswordChangeRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)