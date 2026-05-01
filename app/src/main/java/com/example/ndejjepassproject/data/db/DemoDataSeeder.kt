package com.example.ndejjepassproject.data.db

import android.content.Context
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import java.security.MessageDigest
import com.example.ndejjepassproject.data.db.DataStoreManager

object DemoDataSeeder {

    suspend fun seedIfNeeded(context: Context, db: ClearanceDatabase) {

        val store = DataStoreManager(context)

        // ✅ run only once
        if (store.isDatabaseSeeded()) return

        val students = listOf(
            StudentSeed(
                name = "NABAYA NESTROY",
                email = "nestroy@stud.ndejjeuniversity.ac.ug",
                regNumber = "24/2/306/D/053",
                role = "student",
                year = 2,
                semester = 1,
                programCode = "306",
                programName = "BSc Software Engineering",
                setupComplete = true
            ),

            StudentSeed(
                name = "KUUWE EMMANUEL",
                email = "emmanuel@stud.ndejjeuniversity.ac.ug",
                regNumber = "24/2/306/D/402",
                role = "student",
                year = 3,
                semester = 2,
                programCode = "308",
                programName = "BSc Information Systems",
                setupComplete = true
            ),

            StudentSeed(
                name = "WANDERA EDRIS",
                email = "edris@stud.ndejjeuniversity.ac.ug",
                regNumber = "24/2/306/D/91",
                role = "student",
                year = 1,
                semester = 2,
                programCode = "305",
                programName = "BSc Computer Science",
                setupComplete = true
            ),

            StudentSeed(
                name = "MBONEIREKU CYRUS",
                email = "cyrus@stud.ndejjeuniversity.ac.ug",
                regNumber = "24/2/306/DJ/89",
                role = "student",
                year = 2,
                semester = 1,
                programCode = "306",
                programName = "BSc Software Engineering",
                setupComplete = true
            ),

            StudentSeed(
                name = "KABASIITA VICTORIA",
                email = "victoria@stud.ndejjeuniversity.ac.ug",
                regNumber = "24/2/306/D/246",
                role = "student",
                year = 2,
                semester = 1,
                programCode = "307",
                programName = "BSc Cyber Security",
                setupComplete = true
            ),

            StudentSeed(
                name = "Finance Admin",
                email = "admin@ndejjeuniversity.ac.ug",
                regNumber = "NDU/STAFF/ADMIN",
                role = "admin",
                year = 1,
                semester = 1,
                programCode = "STAFF",
                programName = "Staff",
                setupComplete = true
            ),

            StudentSeed(
                name = "Security Desk",
                email = "security@ndejjeuniversity.ac.ug",
                regNumber = "NDU/STAFF/SEC",
                role = "security",
                year = 1,
                semester = 1,
                programCode = "STAFF",
                programName = "Staff",
                setupComplete = true
            )
        )

        students.forEach {
            seedStudent(db, it)
        }

        store.setDatabaseSeeded()
    }

    private suspend fun seedStudent(
        db: ClearanceDatabase,
        seed: StudentSeed
    ) {
        val existing = db.studentDao().getStudentByEmail(seed.email)
            ?: db.studentDao().getStudentByRegNumber(seed.regNumber)

        if (existing != null) return

        db.studentDao().insertStudent(
            StudentEntity(
                name = seed.name,
                email = seed.email,
                passwordHash = hash("password123"),
                regNumber = seed.regNumber,
                programCode = seed.programCode,
                programName = seed.programName,
                year = seed.year,
                semester = seed.semester,
                role = seed.role,
                isSetupComplete = seed.setupComplete
            )
        )

        val inserted = db.studentDao().getStudentByEmail(seed.email) ?: return

        db.clearanceDao().insertClearance(
            ClearanceEntity(
                studentId = inserted.id,
                tuitionPaid = if (seed.role == "student") 500_000.0 else 0.0,
                percentage = if (seed.role == "student") 27 else 0,
                isCleared = false
            )
        )
    }

    private fun hash(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(value.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}