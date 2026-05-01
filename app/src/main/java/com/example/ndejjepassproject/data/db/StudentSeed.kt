package com.example.ndejjepassproject.data.db

data class StudentSeed(
    val name: String,
    val email: String,
    val regNumber: String,
    val role: String,
    val year: Int,
    val semester: Int,
    val programCode: String,
    val programName: String,
    val setupComplete: Boolean
)