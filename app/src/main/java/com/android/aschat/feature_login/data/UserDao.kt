package com.android.aschat.feature_login.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.aschat.feature_login.domain.model.login.UserInfo

@Dao
interface UserDao {
    @Query("select * from userinfo where userId == :userId")
    suspend fun queryByUserId(userId: String): UserInfo

    @Insert
    suspend fun insertUserInfo(userInfo: UserInfo)

    @Update
    suspend fun updateUserInfo(userInfo: UserInfo): Int
}