package com.example.ndejjepassproject.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_units")
data class CourseUnitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val courseCode: String,
    val courseName: String,
    val creditUnits: Int,
    val isRetake: Boolean = false,
    val semester: Int = 1,
    val year: Int = 1
)