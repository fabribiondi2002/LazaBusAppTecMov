package com.iua.gpi.lazabus.service.interf

import kotlinx.coroutines.flow.StateFlow

interface SttServiceI {
    val recognizedText: StateFlow<String>
    val isListening: StateFlow<Boolean>
    fun startListening()
    fun stopListening()
}