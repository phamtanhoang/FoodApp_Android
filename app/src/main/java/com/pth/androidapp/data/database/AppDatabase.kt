package com.pth.androidapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pth.androidapp.data.database.AppDatabase.Companion.DATABASE_VERSION
import com.pth.androidapp.data.database.daos.UserDao
import com.pth.androidapp.data.database.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "app_database"
        const val DATABASE_VERSION = 1
    }

    abstract fun userDao(): UserDao
}