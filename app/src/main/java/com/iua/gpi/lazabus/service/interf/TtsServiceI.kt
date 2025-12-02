package com.iua.gpi.lazabus.service.interf

import kotlinx.coroutines.flow.StateFlow

interface TtsServiceI {

    val isMotorReady: StateFlow<Boolean>
    /** Inicializa el motor TTS, si es necesario. */
    fun initialize()

    /** Pone el motor TTS a hablar el texto proporcionado. */
    fun speak(text: String)

    /** Libera los recursos del motor TTS. */
    fun shutdown()

    fun setSpeed(speed: Float)
    fun getSpeed(): Float

    /** Controla si el motor está listo para hablar. (Opcional, pero útil) */
    val isInitialized: Boolean


}