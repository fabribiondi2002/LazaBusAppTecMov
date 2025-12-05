package com.iua.gpi.lazabus.service.interf

import kotlinx.coroutines.flow.StateFlow

/**
 * Archivo que contiene las interfaces para inyectar dependencias de TextToSpeech.
 */
interface TtsServiceI {

    val isMotorReady: StateFlow<Boolean>
    fun initialize()
    fun speak(text: String)
    fun shutdown()
    fun setSpeed(speed: Float)
    fun getSpeed(): Float
    val isInitialized: Boolean
    fun setLanguage(lang: String)
    fun getLanguaje(): String


}