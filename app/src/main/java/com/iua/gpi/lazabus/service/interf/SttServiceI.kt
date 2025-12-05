package com.iua.gpi.lazabus.service.interf

import kotlinx.coroutines.flow.StateFlow

/**
 * Archivo que contiene las interfaces para inyectar dependencias de SpeachToText.
 */
interface SttServiceI {
    val recognizedText: StateFlow<String>
    val isListening: StateFlow<Boolean>
    val recognitionError : StateFlow<Boolean>
    fun startListening()
    fun stopListening()
    fun clearText()
}