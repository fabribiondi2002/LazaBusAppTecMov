package com.iua.gpi.lazabus.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase que maneja las preferencias de la aplicación.
 */
@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("lazabus_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SPEED = "tts_speed"
        private const val DEFAULT_SPEED = 0.8f
        private const val KEY_LANGUAGE = "tts_language"
        private const val DEFAULT_LANGUAGE = "es"
    }

    fun saveSpeed(speed: Float) {
        prefs.edit().putFloat(KEY_SPEED, speed).apply()
    }

    fun getSpeed(): Float {
        return prefs.getFloat(KEY_SPEED, DEFAULT_SPEED)
    }
    fun saveLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }
}
