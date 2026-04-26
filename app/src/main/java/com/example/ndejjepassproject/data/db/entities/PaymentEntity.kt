package com.example.ndejjepassproject.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val amount: Double,
    val reference: String,
    val method: String,
    val status: String = "pending",
    val rejectionReason: String? = null,
    val submittedAt: Long = System.currentTimeMillis()
)