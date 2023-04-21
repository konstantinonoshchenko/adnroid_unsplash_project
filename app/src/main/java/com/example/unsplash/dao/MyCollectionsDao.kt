package com.example.unsplash.dao

import androidx.room.*
import com.example.unsplash.data.database.MyCollections

@Dao
interface MyCollectionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyCollections(myCollections: MyCollections)

    @Query("SELECT * FROM `myCollections`")
    suspend fun getMyCollections():List<MyCollections>

    @Delete
    suspend fun deleteMyCollection(myCollections: MyCollections)

    @Query("DELETE FROM `myCollections`")
    suspend fun allDeleteMyCollections()

}