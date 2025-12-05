package com.iua.gpi.lazabus.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.interaction.Dialogos
import com.iua.gpi.lazabus.service.interf.TtsServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel para el TTS.
 */
@HiltViewModel
class TtsViewModel @Inject constructor(
    private val ttsService: TtsServiceI
) : ViewModel(){

    private val _speed = MutableStateFlow(ttsService.getSpeed())
    val speed: StateFlow<Float> = _speed
    private val _language = MutableStateFlow(ttsService.getLanguaje())
    val language = _language.asStateFlow()

    fun updateLanguage(lang: String) {
        _language.value = lang
        Dialogos.setIdioma(lang)
        ttsService.setLanguage(lang)
    }


    init {
        ttsService.initialize()
        Dialogos.setIdioma(ttsService.getLanguaje())
    }

    val isTtsReady: StateFlow<Boolean> = ttsService.isMotorReady
        .stateIn(
            scope = viewModelScope,
            // Permite que el Flow se detenga 5 segundos después de que la UI deje de observarlo
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )


    // La función de negocio que llama el Composable
    fun saludar() {
        if (ttsService.isInitialized) {
            ttsService.speak("Bienvenido a Laza Bus... ¿A dónde te interesaría ir hoy?")
        } else {
            // Manejo de error o espera
            Log.w("TTSVM", "Servicio de saludo no disponible aún.")
        }
    }

    fun hablar(text: String)
    {
        if (ttsService.isInitialized) {
            ttsService.speak(text)
        } else {
            Log.w("TTSVM", "Servicio de hablar no disponible aún.")
        }
    }


    fun updateSpeed(newSpeed: Float) {
        _speed.value = newSpeed
        ttsService.setSpeed(newSpeed)
    }
    /** Llama a shutdown() cuando el ViewModel se destruye. */
    override fun onCleared() {
        ttsService.shutdown()
        super.onCleared()
    }


}