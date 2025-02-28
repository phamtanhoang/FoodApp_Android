package com.pth.androidapp.data.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.pth.androidapp.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Query("select * from user")
    fun getAll(): List<UserEntity>

}