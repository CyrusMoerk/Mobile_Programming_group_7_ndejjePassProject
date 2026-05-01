package com.example.ndejjepassproject.data.db.dao

import androidx.room.*
import com.example.ndejjepassproject.data.db.entities.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    @Query("SELECT * FROM students WHERE email = :email LIMIT 1")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    fun getStudentById(id: Int): Flow<StudentEntity?>

    @Query("SELECT * FROM students WHERE role = 'student' ORDER BY name ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE regNumber = :regNumber LIMIT 1")
    suspend fun getStudentByRegNumber(regNumber: String): StudentEntity?

    @Update
    suspend fun updateStudent(student: StudentEntity)
}