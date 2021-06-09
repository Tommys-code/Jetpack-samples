package com.tommy.shen.room_sample.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tommy.shen.room_sample.sqlite.DatabaseHelper

@Entity(tableName = DatabaseHelper.TABLE_NAME)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

