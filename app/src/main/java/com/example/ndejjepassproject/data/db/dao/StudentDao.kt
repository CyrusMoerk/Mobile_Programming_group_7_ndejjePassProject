// data/db/dao/StudentDao.kt
// All SQL for the students table.
// Flow return types mean Room pushes updates automatically — no manual refresh.

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    // Used during login — checks email exists before comparing password
    @Query("SELECT * FROM students WHERE email = :email LIMIT 1")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    // Returns Flow → dashboard auto-updates if student data changes
    @Query("SELECT * FROM students WHERE id = :id LIMIT 1")
    fun getStudentById(id: Int): Flow<StudentEntity?>

    @Update
    suspend fun updateStudent(student: StudentEntity)
}