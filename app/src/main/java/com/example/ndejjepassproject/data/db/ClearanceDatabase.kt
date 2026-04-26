package com.example.ndejjepassproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ndejjepassproject.data.db.dao.ClearanceDao
import com.example.ndejjepassproject.data.db.dao.PaymentDao
import com.example.ndejjepassproject.data.db.dao.StudentDao
import com.example.ndejjepassproject.data.db.entities.ClearanceEntity
import com.example.ndejjepassproject.data.db.entities.PaymentEntity
import com.example.ndejjepassproject.data.db.entities.StudentEntity

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
        @Volatile
        private var INSTANCE: ClearanceDatabase? = null

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