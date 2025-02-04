package com.example.newp.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import com.example.newp.db.Data
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface DaoClass{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Data)

    @Query("SELECT * FROM News where state = :state")
    suspend fun getNews(state: String): List<Data>

    @Query("DELETE FROM News where timestamp < :timestamp")
    suspend fun delNews( timestamp: Long)

    @Query("SELECT timestamp from News where state = :state")
    suspend fun gettimestamp(state: String): Long



}