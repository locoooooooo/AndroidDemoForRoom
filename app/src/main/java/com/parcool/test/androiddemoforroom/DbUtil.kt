package com.parcool.test.androiddemoforroom

import android.content.Context
import androidx.room.Room

object DbUtil {

    var appDatabase: AppDatabase? = null;

    fun init(applicationContext: Context): AppDatabase {
        return appDatabase ?: Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    fun getUserDao(applicationContext: Context): UserDao {
        return appDatabase?.userDao() ?: init(applicationContext).userDao()
    }
}