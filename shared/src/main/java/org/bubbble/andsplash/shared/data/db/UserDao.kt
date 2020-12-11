package org.bubbble.andsplash.shared.data.db

import androidx.room.*

/**
 * @author Andrew
 * @date 2020/12/08 10:37
 */

@Dao
interface UserDao {

    @Query("SELECT * FROM userentity WHERE numeric_id = :currentUserId")
    suspend fun getCurrentUser(currentUserId: Int): List<UserEntity>

    @Query("SELECT * FROM userentity")
    suspend fun getAll(): List<UserEntity>

    @Query("SELECT * FROM userentity WHERE numeric_id NOT IN(:currentUserId)")
    suspend fun getNotCurrentAll(currentUserId: Int): List<UserEntity>

    @Query("SELECT COUNT(numeric_id) FROM userentity WHERE numeric_id = :currentUserId")
    suspend fun getUserExistById(currentUserId: Int): Int

    @Insert
    suspend fun setAll(vararg user: UserEntity)

    @Update
    suspend fun updateCurrentUser(vararg user: UserEntity)

    @Query("DELETE FROM userentity WHERE numeric_id = (:currentUserId)")
    suspend fun removeUserById(currentUserId: Int)
}