package com.example.ndejjepassproject.data.db

import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import java.security.MessageDigest

object DemoDataSeeder {

    suspend fun seedIfNeeded(db: ClearanceDatabase) {
        seedStudent(
            db = db,
            name = "Student Demo",
            email = "student@stud.ndejjeuniversity.ac.ug",
            regNumber = "NDU/S20/0001",
            role = "student",
            year = 2,
            semester = 1,
            setupComplete = true
        )
        seedStudent(
            db = db,
            name = "Finance Admin",
            email = "admin@ndejjeuniversity.ac.ug",
            regNumber = "NDU/STAFF/ADMIN",
            role = "admin",
            year = 1,
            semester = 1,
            setupComplete = true
        )
        seedStudent(
            db = db,
            name = "Security Desk",
            email = "security@ndejjeuniversity.ac.ug",
            regNumber = "NDU/STAFF/SEC",
            role = "security",
            year = 1,
            semester = 1,
            setupComplete = true
        )
    }

    private suspend fun seedStudent(
        db: ClearanceDatabase,
        name: String,
        email: String,
        regNumber: String,
        role: String,
        year: Int,
        semester: Int,
        setupComplete: Boolean
    ) {
        val existing = db.studentDao().getStudentByEmail(email)
        if (existing != null) return

        db.studentDao().insertStudent(
            StudentEntity(
                name = name,
                email = email,
                passwordHash = hash("password123"),
                regNumber = regNumber,
                programCode = "306",
                programName = "BSc Software Engineering",
                year = year,
                semester = semester,
                role = role,
                isSetupComplete = setupComplete
            )
        )

        val inserted = db.studentDao().getStudentByEmail(email) ?: return
        db.clearanceDao().insertClearance(
            ClearanceEntity(
                studentId = inserted.id,
                tuitionPaid = if (role == "student") 500_000.0 else 0.0,
                percentage = if (role == "student") 27 else 0,
                isCleared = false
            )
        )
    }

    private fun hash(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(value.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
