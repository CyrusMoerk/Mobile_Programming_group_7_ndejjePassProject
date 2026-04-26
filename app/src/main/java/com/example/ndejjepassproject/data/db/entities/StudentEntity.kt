package com.example.ndejjepassproject.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val regNumber: String,
    val programCode: String = "306",
    val programName: String = "",
    val year: Int = 1,
    val semester: Int = 1,
    val role: String = "student",
    val isSetupComplete: Boolean = false
)