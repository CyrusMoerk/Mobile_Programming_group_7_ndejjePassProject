package com.example.ndejjepassproject.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clearance")
data class ClearanceEntity(
    @PrimaryKey val studentId: Int,
    val tuitionDue: Double = 1_800_000.0,
    val tuitionPaid: Double = 0.0,
    val percentage: Int = 0,
    val isCleared: Boolean = false
)