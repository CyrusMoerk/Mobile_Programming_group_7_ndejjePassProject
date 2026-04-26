package com.example.ndejjepassproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Update
    suspend fun updateStudent(student: StudentEntity)
}