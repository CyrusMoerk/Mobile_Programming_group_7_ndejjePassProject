package com.example.ndejjepassproject.data.model

data class StudentUpdateRequest(
    val name: String,
    val year: Int,
    val semester: Int
)

data class PasswordChangeRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)