package org.bubbble.andsplash.shared.data.db

import android.nfc.Tag
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * @author Andrew
 * @date 2020/12/08 10:36
 */
@Entity(tableName = "photoentity", indices = [Index(value = ["id"], unique = true)])
data class PhotoEntity (
     @PrimaryKey val id: String,
     val created_at: String?,
     val updated_at: String?,
     val width: Int?,
     val height: Int?,
     val color: String?,
     val downloads: Int?,
     val likes: Int?,
     val liked_by_user: Boolean?,
     val description: String?,
     val user_name: String?,
     val user_profile: String?,
     val urls: String,
) {
    companion object {
        fun getDefault() = PhotoEntity(
            "",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "",
        )
    }
}