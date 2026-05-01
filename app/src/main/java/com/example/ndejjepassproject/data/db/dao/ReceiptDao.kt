package com.example.ndejjepassproject.data.db.dao

import androidx.room.*
import com.example.ndejjepassproject.data.db.entities.ReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ReceiptEntity): Long

    @Query("SELECT * FROM receipts WHERE studentId = :studentId ORDER BY submittedAt DESC")
    fun getReceiptsForStudent(studentId: Int): Flow<List<ReceiptEntity>>

    @Query("SELECT * FROM receipts WHERE status = 'pending' ORDER BY submittedAt DESC")
    fun getPendingReceipts(): Flow<List<ReceiptEntity>>

    @Query("UPDATE receipts SET status = :status WHERE id = :id")
    suspend fun updateReceiptStatus(id: Int, status: String)
}