package com.tommy.shen.paging3sample.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PersonDao {

    @Query("SELECT * FROM PersonData ORDER BY id desc")
    fun allPerson(): PagingSource<Int, PersonData>

    @Insert
    suspend fun insert(person: List<PersonData>)

    @Delete
    suspend fun delete(cheese: PersonData)
}