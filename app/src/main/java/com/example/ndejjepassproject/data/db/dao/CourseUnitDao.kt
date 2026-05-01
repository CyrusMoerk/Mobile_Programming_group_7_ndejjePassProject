package com.example.ndejjepassproject.data.db.dao

import androidx.room.*
import com.example.ndejjepassproject.data.db.entities.CourseUnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseUnitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseUnits(courses: List<CourseUnitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseUnit(course: CourseUnitEntity)

    @Query("SELECT * FROM course_units WHERE studentId = :studentId")
    fun getCourseUnitsForStudent(studentId: Int): Flow<List<CourseUnitEntity>>

    @Query("DELETE FROM course_units WHERE studentId = :studentId")
    suspend fun clearCoursesForStudent(studentId: Int)
}