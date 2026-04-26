// data/db/entities/StudentEntity.kt
// Comments now clearly mark every field as EDITABLE or LOCKED.
// The entity itself does not enforce the lock — the repository does.

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,                          // SYSTEM — never touched


    val name: String,                           // EDITABLE by student


    val email: String,                          // LOCKED — university assigned

    val passwordHash: String,                  // EDITABLE via change-password only


    val regNumber: String,                     // LOCKED — auto-generated once


    val programCode: String = "306",           // LOCKED — the 306, never changes


    val programName: String = "",              // LOCKED — set once at academic setup



    val year: Int = 1,                         // EDITABLE by student


    val semester: Int = 1,                     // EDITABLE by student



    val role: String = "student",              // LOCKED — admin assigns, student cannot change

    val isSetupComplete: Boolean = false       // SYSTEM — set by app, not user
)