package com.example.ndejjepassproject.data.db.relations

data class PaymentWithReceipt(
    val id: Int,
    val studentId: Int,
    val amount: Double,
    val method: String,
    val status: String,
    val submittedAt: Long,

    val filePath: String?,   // from Receipt
    val fileType: String?
)