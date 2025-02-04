package com.example.newp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "News")
data class Data(
@PrimaryKey(autoGenerate = true)val id : Int = 0,
val state :String,
    val description : String,
    var timestamp: Long = System.currentTimeMillis()
)
