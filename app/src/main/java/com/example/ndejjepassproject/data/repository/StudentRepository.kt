package com.example.ndejjepassproject.data.repository

import com.example.ndejjepassproject.data.db.dao.ClearanceDao
import com.example.ndejjepassproject.data.db.dao.CourseUnitDao
import com.example.ndejjepassproject.data.db.dao.StudentDao
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.CourseUnitEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import com.example.ndejjepassproject.data.model.PasswordChangeRequest
import com.example.ndejjepassproject.data.model.StudentUpdateRequest
import java.security.MessageDigest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class StudentRepository(
    private val studentDao: StudentDao,
    private val clearanceDao: ClearanceDao,
    private val courseUnitDao: CourseUnitDao
) {

    suspend fun login(email: String, password: String): Result<StudentEntity> {
        return try {
            val student = studentDao.getStudentByEmail(email.trim())
                ?: return Result.failure(Exception("Account not found"))
            if (student.passwordHash != hashPassword(password)) {
                return Result.failure(Exception("Invalid email or password"))
            }
            Result.success(student)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getStudent(studentId: Int): Flow<StudentEntity?> =
        studentDao.getStudentById(studentId)

    fun getClearance(studentId: Int): Flow<ClearanceEntity?> =
        clearanceDao.getClearanceByStudent(studentId)

    fun getAllStudents(): Flow<List<StudentEntity>> =
        studentDao.getAllStudents()

    fun getCourseUnits(studentId: Int): Flow<List<CourseUnitEntity>> =
        courseUnitDao.getCourseUnitsForStudent(studentId)

    suspend fun saveCourseUnits(
        studentId: Int,
        courses: List<CourseUnitEntity>
    ): Result<Unit> {
        return try {
            courseUnitDao.clearCoursesForStudent(studentId)
            courseUnitDao.insertCourseUnits(courses)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        studentId: Int,
        request: StudentUpdateRequest
    ): Result<Unit> {
        return try {
            val existing = studentDao.getStudentById(studentId).first()
                ?: return Result.failure(Exception("Student not found"))
            if (request.name.isBlank())
                return Result.failure(Exception("Name cannot be empty"))
            studentDao.updateStudent(
                existing.copy(
                    name     = request.name.trim(),
                    year     = request.year,
                    semester = request.semester,
                    hall     = request.hall,
                    nationality = request.nationality,
                    studyMode   = request.studyMode,
                    intake      = request.intake
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePhotoPath(studentId: Int, path: String): Result<Unit> {
        return try {
            val existing = studentDao.getStudentById(studentId).first()
                ?: return Result.failure(Exception("Student not found"))
            studentDao.updateStudent(existing.copy(photoPath = path))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(
        studentId: Int,
        request: PasswordChangeRequest
    ): Result<Unit> {
        return try {
            val existing = studentDao.getStudentById(studentId).first()
                ?: return Result.failure(Exception("Student not found"))
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}