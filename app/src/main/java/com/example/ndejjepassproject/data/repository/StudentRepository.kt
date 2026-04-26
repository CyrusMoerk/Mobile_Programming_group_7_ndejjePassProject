// data/repository/StudentRepository.kt
// updateProfile() is the key function.
// It fetches the CURRENT entity from the database first,
// then uses .copy() to apply ONLY the allowed fields.
// Whatever the student passes for email/role/regNumber — ignored.

class StudentRepository(
    private val studentDao: StudentDao,
    private val clearanceDao: ClearanceDao
) {

    // ... existing login(), getStudent(), getClearance() stay the same ...

    // UPDATE PROFILE — only name, year, semester are applied.
    // All locked fields come from the existing database row — untouched.
    suspend fun updateProfile(
        studentId: Int,
        request: StudentUpdateRequest
    ): Result<Unit> {
        return try {
            // Step 1: Fetch the real current entity from database
            val existing = studentDao.getStudentById(studentId).first()
                ?: return Result.failure(Exception("Student not found"))

            // Step 2: Validate the request before applying
            if (request.name.isBlank())
                return Result.failure(Exception("Name cannot be empty"))
            if (request.year !in 1..5)
                return Result.failure(Exception("Year must be between 1 and 5"))
            if (request.semester !in 1..2)
                return Result.failure(Exception("Semester must be 1 or 2"))

            // Step 3: .copy() with ONLY the 3 editable fields.
            // email, regNumber, programCode, programName, role, passwordHash
            // are ALL taken from 'existing' — student cannot touch them.
            studentDao.updateStudent(
                existing.copy(
                    name     = request.name.trim(),
                    year     = request.year,
                    semester = request.semester
                    // every other field inherits from 'existing' automatically
                )
            )
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    // CHANGE PASSWORD — separate function, separate validation flow.
    // Requires current password to be correct before allowing change.
    suspend fun changePassword(
        studentId: Int,
        request: PasswordChangeRequest
    ): Result<Unit> {
        return try {
            val existing = studentDao.getStudentById(studentId).first()
                ?: return Result.failure(Exception("Student not found"))

            // Must verify current password first
            if (existing.passwordHash != hashPassword(request.currentPassword))
                return Result.failure(Exception("Current password is incorrect"))
            if (request.newPassword != request.confirmPassword)
                return Result.failure(Exception("Passwords do not match"))
            if (request.newPassword.length < 8)
                return Result.failure(Exception("Password must be at least 8 characters"))

            studentDao.updateStudent(
                existing.copy(passwordHash = hashPassword(request.newPassword))
            )
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
