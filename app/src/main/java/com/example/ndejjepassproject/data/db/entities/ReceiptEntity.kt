package com.example.ndejjepassproject.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipts")
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val paymentId: Int,
    val filePath: String,
    val fileType: String, // "image" or "pdf"
    val amount: Double,
    val method: String,
    val status: String = "pending",
    val submittedAt: Long = System.currentTimeMillis()
)