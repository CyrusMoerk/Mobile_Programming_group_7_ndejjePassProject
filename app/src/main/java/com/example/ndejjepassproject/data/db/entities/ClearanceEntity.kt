// data/db/entities/ClearanceEntity.kt
// One row per student. Tracks tuition progress only.
// percentage and isCleared are recalculated on every admin approval.

@Entity(tableName = "clearance")
data class ClearanceEntity(
    @PrimaryKey val studentId: Int,
    val tuitionDue: Double = 1_800_000.0, // set by admin — only tuition
    val tuitionPaid: Double = 0.0,
    val percentage: Int = 0,              // 0–100, derived field
    val isCleared: Boolean = false        // true → QR permit unlocked
)