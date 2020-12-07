package org.bubbble.andsplash.shared.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesUtil @Inject constructor(
    @ApplicationContext val context: Context
) {

    init {
        logger("PreferencesUtil：初始化了一次！！！！")
    }

    fun <T> put(key: String, value: T) {
        val mShareConfig = PreferenceManager.getDefaultSharedPreferences(context)
        put(mShareConfig, key, value)
    }

    private fun <T> put(mShareConfig: SharedPreferences, key: String, value: T) {
        if (notNull(value)) {
            val conEdit = mShareConfig.edit()
            when (value) {
                is String -> conEdit.putString(key, (value as String).trim())
                is Long -> conEdit.putLong(key, value as Long)
                is Boolean -> conEdit.putBoolean(key, value as Boolean)
                is Int -> conEdit.putInt(key, value as Int)
                is Float -> conEdit.putFloat(key, value as Float)
            }
            conEdit.apply()
        }
    }

    fun <T> get(key: String, defValue: T): T {
        val mShareConfig = PreferenceManager.getDefaultSharedPreferences(context)
//        Log.d("PreferencesUtil", Thread.currentThread().name)
        return get(mShareConfig, key, defValue)
    }

    private fun <T> get(mShareConfig: SharedPreferences, key: String, defValue: T): T {
        var value: T? = null
        if (notNull(key)) {
            when (defValue) {
                is String -> value = mShareConfig.getString(key, defValue as String) as T
                is Long -> value = java.lang.Long.valueOf(mShareConfig.getLong(key, defValue as Long)) as T
                is Boolean -> value = java.lang.Boolean.valueOf(mShareConfig.getBoolean(key, defValue as Boolean)) as T
                is Int -> value = Integer.valueOf(mShareConfig.getInt(key, defValue as Int)) as T
                is Float -> value = java.lang.Float.valueOf(mShareConfig.getFloat(key, defValue as Float)) as T
            }
        }
        return value?:defValue
    }

    /**
     * object not null
     */
    private fun notNull(obj: Any?): Boolean {
        return null != obj
    }

}