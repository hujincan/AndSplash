package org.bubbble.andsplash.shared.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * @author Andrew
 * @date 2020/12/08 10:37
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM userentity")
    suspend fun getAll(): List<UserEntity>

    @Insert
    suspend fun saveAll(user: UserEntity)
}