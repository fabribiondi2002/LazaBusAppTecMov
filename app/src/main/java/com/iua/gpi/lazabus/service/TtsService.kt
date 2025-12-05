package com.iua.gpi.lazabus.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import com.iua.gpi.lazabus.service.interf.TtsServiceI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import com.iua.gpi.lazabus.data.AppPreferences
import com.iua.gpi.lazabus.interaction.Dialogos

/**
 * Servicio de TextToSpeech que implementa la interfaz TtsServiceI
 */
class TtsService @Inject constructor(
    private val context: Context,
    private val prefs: AppPreferences

) : TtsServiceI, TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null

    private val _isMotorReady = MutableStateFlow(false)
    override val isMotorReady: StateFlow<Boolean> = _isMotorReady.asStateFlow()
    private var currentSpeed: Float = prefs.getSpeed()

    override var isInitialized: Boolean = false
        private set

    /**
     * Inicializa el servicio de TextToSpeech
     */
    override fun initialize() {
        if (tts == null) {
            tts = TextToSpeech(context, this)
        }
    }

    /**
     * Cuando el servicio de TextToSpeech se inicia configura el idioma, el tono y la velocidad de salida.
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val langCode = prefs.getLanguage()
            Dialogos.setIdioma(langCode)
            val locale = when (langCode) {
                "en" -> Locale.US
                else -> Locale("es", "AR")
            }

            val result = tts?.setLanguage(locale)


            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {

                tts?.setSpeechRate(currentSpeed)
                Log.i("TTS", "Velocidad de habla ajustada a: $currentSpeed")

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

    /**
     * Habla el texto proporcionado
     */
    override fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text.hashCode().toString())
        } else {
            Log.w("TTS", "TTS no inicializado, omitiendo la función speak().")
        }
    }

    /**
     * Establece la velocidad de habla
     */
    override fun setSpeed(speed: Float) {
        currentSpeed = speed
        prefs.saveSpeed(speed)
        tts?.setSpeechRate(speed)
        Log.i("TTS", "Velocidad del TTS actualizada: $speed")
    }

    /**
     * Obtiene la velocidad de habla
     */
    override fun getSpeed(): Float = currentSpeed

    /**
     * Libera el servicio de TextToSpeech
     */
    override fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        Log.i("TTS", "Servicio TTS Liberado.")
    }


    override fun setLanguage(lang: String) {
        prefs.saveLanguage(lang)

        tts?.language = when (lang) {
            "en" -> Locale.US
            else -> Locale("es", "AR")
        }
    }
    override fun getLanguaje(): String = prefs.getLanguage()

}
