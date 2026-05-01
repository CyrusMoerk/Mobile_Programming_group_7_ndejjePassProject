package com.example.ndejjepassproject.data.model

data class StudentUpdateRequest(
    val name: String,
    val year: Int,
    val semester: Int,
    val hall: String = "",
    val nationality: String = "Ugandan",
    val studyMode: String = "Day",
    val intake: String = "August"
)

data class PasswordChangeRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)