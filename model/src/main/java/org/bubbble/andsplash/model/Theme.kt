package org.bubbble.andsplash.model

/**
 * @author Andrew
 * @date 2020/10/20 13:50
 */
enum class Theme(val storageKey: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system"),
    BATTERY_SAVER("battery_saver")
}

/**
 * Returns the matching [Theme] for the given [storageKey] value.
 */
fun themeFromStorageKey(storageKey: String): Theme? {
    return Theme.values().firstOrNull { it.storageKey == storageKey }
}
