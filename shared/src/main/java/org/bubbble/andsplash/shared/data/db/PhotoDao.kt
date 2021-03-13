package org.bubbble.andsplash.shared.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * @author Andrew
 * @date 2021/02/06 17:51
 */
@Dao
interface PhotoDao {

    @Query("DELETE FROM photoentity")
    suspend fun clearAllPhoto()

    @Insert
    suspend fun saveAllPhoto(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photoentity LIMIT :limit OFFSET :offset")
    suspend fun getAllPhoto(limit: Int, offset: Int): List<PhotoEntity>
}