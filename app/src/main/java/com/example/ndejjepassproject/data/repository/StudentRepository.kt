//data/repository/StudentRepository.kt
// Handles login validation, academic setup, and student data reads.
// This is where business rules for authentication live.

class StudentRepository(
    private val studentDao: StudentDao,
    private val clearanceDao: ClearanceDao
) {
    // LOGIN — two validations before hitting the database:
    // 1. Email domain must be university email
    // 2. Password hash must match what is stored
    suspend fun login(email: String, password: String): Result<StudentEntity> {
        if (!email.endsWith("@stud.ndejjeuniversity.ac.ug")) {
            return Result.failure(Exception("Use your university email address"))
        }
        val student = studentDao.getStudentByEmail(email)
            ?: return Result.failure(Exception("Account not found"))
        if (student.passwordHash != hashPassword(password)) {
            return Result.failure(Exception("Incorrect password"))
        }
        return Result.success(student)
    }

    // Returns Flow — dashboard screen observes this and updates automatically
    fun getStudent(id: Int): Flow<StudentEntity?> = studentDao.getStudentById(id)

    fun getClearance(id: Int): Flow<ClearanceEntity?> = clearanceDao.getClearanceByStudent(id)

    // Called on first login — saves program choice and creates clearance row
    suspend fun completeAcademicSetup(
        studentId: Int, programName: String, year: Int, semester: Int
    ) {
        studentDao.getStudentById(studentId).first()?.let { existing ->
            studentDao.updateStudent(existing.copy(
                programName = programName,
                year = year,
                semester = semester,
                isSetupComplete = true
            ))
        }
        // Seed the clearance row with tuition due = 1,800,000 (tuition only)
        clearanceDao.insertClearance(ClearanceEntity(studentId = studentId))
    }

    // SHA-256 hash — passwords never stored as plain text
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}