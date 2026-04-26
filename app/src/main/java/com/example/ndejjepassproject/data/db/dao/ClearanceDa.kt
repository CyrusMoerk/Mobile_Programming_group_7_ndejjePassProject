// data/db/dao/ClearanceDao.kt
// Reads and writes the clearance table.
// updateProgress is called automatically by the repository
// every time a payment is approved.

@Dao
interface ClearanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClearance(clearance: ClearanceEntity)

    // Dashboard observes this — auto-updates the progress bar
    @Query("SELECT * FROM clearance WHERE studentId = :studentId LIMIT 1")
    fun getClearanceByStudent(studentId: Int): Flow<ClearanceEntity?>

    // Called by repository after every admin approval
    @Query("UPDATE clearance SET tuitionPaid = :paid, percentage = :pct, isCleared = :cleared WHERE studentId = :sid")
    suspend fun updateProgress(sid: Int, paid: Double, pct: Int, cleared: Boolean)
}