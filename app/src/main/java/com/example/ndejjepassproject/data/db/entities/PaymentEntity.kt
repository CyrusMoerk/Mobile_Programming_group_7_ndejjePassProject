
// One row per payment submission. Status flows: pending → approved/rejected.
// Tuition only — exam and functional fees removed.

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val amount: Double,
    val reference: String,          // bank ref or auto-gen for mobile money
    val method: String,             // "mtn" | "airtel" | "bank"
    val status: String = "pending", // pending | approved | rejected
    val rejectionReason: String? = null,
    val submittedAt: Long = System.currentTimeMillis()
)