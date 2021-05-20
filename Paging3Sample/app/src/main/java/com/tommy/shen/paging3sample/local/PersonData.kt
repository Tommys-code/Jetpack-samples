package com.tommy.shen.paging3sample.local

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class PersonData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
) {
    @Ignore
    var delete: Boolean = false
}