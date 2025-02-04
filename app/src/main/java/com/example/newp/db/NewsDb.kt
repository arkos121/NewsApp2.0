package com.example.newp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newp.db.DaoClass
import com.example.newp.db.Data

@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class NewsDb : RoomDatabase() {
    abstract fun newsDao(): DaoClass

    companion object {
        @Volatile
        private var instance: NewsDb? = null  // Make it nullable

        fun getDatabase(context: Context): NewsDb {
            return instance ?: synchronized(this) {
                val newInstance = instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NewsDb::class.java,
                    "newsdb"
                ).build().also { instance = it }
                newInstance
            }
        }
    }
}
