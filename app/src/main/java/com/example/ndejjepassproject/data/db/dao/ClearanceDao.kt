package com.example.ndejjepassproject.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClearanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClearance(clearance: ClearanceEntity)

    @Query("SELECT * FROM clearance WHERE studentId = :studentId LIMIT 1")
    fun getClearanceByStudent(studentId: Int): Flow<ClearanceEntity?>

    @Query("UPDATE clearance SET tuitionPaid = :paid, percentage = :pct, isCleared = :cleared WHERE studentId = :sid")
    suspend fun updateProgress(sid: Int, paid: Double, pct: Int, cleared: Boolean)
}
