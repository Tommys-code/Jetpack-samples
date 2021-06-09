package com.tommy.shen.room_sample.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User ORDER BY id desc")
    fun getAll(): List<User>

    @Query("SELECT name FROM User ORDER BY id desc")
    fun getAllNames(): List<String>

    @Query("SELECT * FROM User WHERE id = :id")
    fun load(id: Int): User

    @Insert
    fun insertAll(user: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun updateUser(user: User)
}