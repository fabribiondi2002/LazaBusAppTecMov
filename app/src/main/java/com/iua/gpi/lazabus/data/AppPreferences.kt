package com.iua.gpi.lazabus.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("lazabus_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_SPEED = "tts_speed"
        private const val DEFAULT_SPEED = 0.8f
    }

    fun saveSpeed(speed: Float) {
        prefs.edit().putFloat(KEY_SPEED, speed).apply()
    }

    fun getSpeed(): Float {
        return prefs.getFloat(KEY_SPEED, DEFAULT_SPEED)
    }
}
