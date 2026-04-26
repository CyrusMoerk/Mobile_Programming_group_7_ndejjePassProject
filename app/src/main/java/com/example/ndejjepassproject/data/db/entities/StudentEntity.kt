// data/db/entities/StudentEntity.kt
// One row per student. Stores login details + academic setup.

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val regNumber: String,       // e.g. 24.2.306.D.402 — auto-generated
    val programCode: String = "306", // LOCKED — never editable by student
    val programName: String = "",
    val year: Int = 1,
    val semester: Int = 1,
    val role: String = "student",       // student | admin | security
    val isSetupComplete: Boolean = false // false → show AcademicSetupScreen first
)