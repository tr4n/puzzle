package com.tr4n.puzzle.util.prefs

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tr4n.puzzle.di.App

object SharedPrefs  {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(SharedPrefKey.PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val context by lazy {
        App.context
    }

    private val gson by lazy {
        Gson()
    }

    fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
            else -> editor.putString(key, gson.toJson(data))
        }
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String, type: Class<T>, default: T?): T? = sharedPreferences.run {
        when (type) {
            String::class.java -> getString(key, default as? String) as? T

            Boolean::class.java -> getBoolean(key, (default as? Boolean) == true) as? T

            Float::class.java -> getFloat(key, default as? Float ?: 0f) as? T

            Int::class.java -> getInt(key, default as? Int ?: 0) as? T

            Long::class.java -> getLong(key, default as? Long ?: 0L) as? T

            else -> gson.fromJson(sharedPreferences.getString(key, default as? String), type)
        }
    }

    inline operator fun <reified T> get(key: String, default: T): T =
        App.context.getSharedPreferences(SharedPrefKey.PREFS_NAME, Context.MODE_PRIVATE)
            .runCatching {
                when (T::class) {
                    String::class -> getString(key, default as? String) as? T

                    Boolean::class -> getBoolean(key, (default as? Boolean) == true) as? T

                    Float::class -> getFloat(key, default as? Float ?: 0f) as? T

                    Int::class -> getInt(key, default as? Int ?: 0) as? T

                    Long::class -> getLong(key, default as? Long ?: 0L) as? T

                    else -> Gson().fromJson(getString(key, default as? String), T::class.java)
                }
            }.getOrNull() ?: default

    inline operator fun <reified T> get(key: String): T? =
        App.context.getSharedPreferences(SharedPrefKey.PREFS_NAME, Context.MODE_PRIVATE)
            .runCatching {
                when (T::class) {
                    String::class -> getString(key, "")

                    Boolean::class -> getBoolean(key, false)

                    Float::class -> getFloat(key, 0f)

                    Int::class -> getInt(key, 0)

                    Long::class -> getLong(key, 0L)

                    else -> Gson().fromJson(getString(key, ""), T::class.java)
                }
            }.getOrNull() as? T

    fun <T> putList(key: String, list: List<T>) {
        val value = gson.toJson(list)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun <T> getList(key: String, clazz: Class<T>): List<T>? {
        val typeOfT = TypeToken.getParameterized(List::class.java, clazz).type
        return gson.fromJson<List<T>>(get(key, String::class.java, null), typeOfT)
    }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
