package com.example.unsplash.dao

import androidx.room.*
import com.example.unsplash.data.database.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM `user`")
    suspend fun getUser():User?

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM `user`")
    suspend fun allDeleteUser()
}