// data/db/ClearanceDatabase.kt
// The single database instance. Singleton pattern with synchronized
// block prevents two threads creating two databases at the same time.

@Database(
    entities = [StudentEntity::class, PaymentEntity::class, ClearanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ClearanceDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun paymentDao(): PaymentDao
    abstract fun clearanceDao(): ClearanceDao

    companion object {
        @Volatile private var INSTANCE: ClearanceDatabase? = null

        fun getInstance(context: Context): ClearanceDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ClearanceDatabase::class.java,
                    "clearance_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}