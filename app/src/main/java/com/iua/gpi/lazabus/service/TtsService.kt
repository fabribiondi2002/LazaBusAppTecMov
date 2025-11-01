package com.iua.gpi.lazabus.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.iua.gpi.lazabus.service.interf.TtsServiceI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale


class TtsService(
    private val context: Context // Usamos Application Context
) : TtsServiceI, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    private val _isMotorReady = MutableStateFlow(false)
    override val isMotorReady: StateFlow<Boolean> = _isMotorReady.asStateFlow()

    override var isInitialized: Boolean = false
        private set

    override fun initialize() {
        if (tts == null) {
            // Inicializa TextToSpeech, pasándole el contexto de la app y el listener
            tts = TextToSpeech(context, this)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "AR"))
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {

                // AJUSTE DE VELOCIDAD (Para que hable más PAUSADO)
                // 1.0f es la velocidad normal. Prueba con 0.8f o 0.7f para ir más lento.
                val speechRate = 0.8f
                tts?.setSpeechRate(speechRate)
                Log.i("TTS", "Velocidad de habla ajustada a: $speechRate")

                // AJUSTE DE TONO
                // 1.0f es el tono normal. Prueba con 1.1f o 0.9f para experimentar.
                val pitch = 0.8f
                tts?.setPitch(pitch)
                Log.i("TTS", "Tono de voz ajustado a: $pitch")

                isInitialized = true
                Log.i("TTS", "Servicio TTS Inicializado correctamente.")
            } else {
                Log.e("TTS", "Error: Idioma no soportado.")
            }
            _isMotorReady.value = true
        } else {
            Log.e("TTS", "Fallo en la inicialización de TTS, estado: $status")
        }
    }

    override fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text.hashCode().toString())
        } else {
            Log.w("TTS", "TTS no inicializado, omitiendo la función speak().")
        }
    }

    override fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        Log.i("TTS", "Servicio TTS Liberado.")
    }
}