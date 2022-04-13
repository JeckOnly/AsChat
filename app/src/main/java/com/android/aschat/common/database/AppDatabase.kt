package com.android.aschat.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.aschat.feature_login.data.UserDao
import com.android.aschat.feature_login.domain.model.login.UserInfo

@Database(entities = [UserInfo::class], version = 1)
@TypeConverters(ClassConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}