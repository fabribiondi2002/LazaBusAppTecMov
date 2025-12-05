package com.iua.gpi.lazabus.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.iua.gpi.lazabus.service.interf.SttServiceI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject

/**
 * Servicio de reconocimiento de voz que implementa la interfaz SttServiceI
 */
class SttService (private val context: Context
) : SttServiceI, RecognitionListener {

    private val TAG = "SttService"

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val _recognizedText = MutableStateFlow("")
    override val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()
    private val _isListening = MutableStateFlow(false)
    override val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    private val _recognitionError = MutableStateFlow(false)
    override val recognitionError: StateFlow<Boolean> = _recognitionError.asStateFlow()

    init {
        speechRecognizer.setRecognitionListener(this)
    }

    /**
     * Inicia la escucha de voz con un intent de reconocimiento de voz.
     */
    override fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechRecognizer.startListening(intent)
        _isListening.value = true
    }

    /**
     * Detiene la escucha de voz.
     */
    override fun stopListening() {
        speechRecognizer.stopListening()
        _isListening.value = false
    }


    /**
     * Prepara el servicio para está cuando listo para recibir la entrada de voz.
     */
    override fun onReadyForSpeech(params: Bundle?) {
        _recognizedText.value = "" // Limpiamos el texto anterior al comenzar una nueva sesión
        _recognitionError.value= false
        Log.d(TAG, "Micrófono listo para hablar.")
    }

    /**
     * Cuando el usuario comienza a hablar se indica por logs.
     */
    override fun onBeginningOfSpeech() {
        Log.d(TAG, "Comienzo de la entrada de voz.")
    }

    /**
     * Cuando el servicio finaliza la entrada de voz se indica por logs.
     */
    override fun onEndOfSpeech() {
        Log.d(TAG, "Fin de la entrada de voz, esperando resultados.")
    }

    /**
     * Cuando el servicio recibe los resultados de la entrada de voz se guardan y se muestran por logs
     */
    override fun onResults(results: Bundle) {
        val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val finalResult = matches?.getOrNull(0) ?: "No se reconoció voz."

        _recognizedText.value = finalResult
        _isListening.value = false // Finalizamos la sesión de escucha
        Log.i(TAG, "Resultado final: $finalResult")
    }

    // Se reciben resultados parciales (mientras el usuario habla).
    override fun onPartialResults(partialResults: Bundle) {
        val matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val partialText = matches?.getOrNull(0) ?: ""

        // Muestra el texto parcial si quieres feedback en tiempo real mientras el usuario habla
        // _recognizedText.value = partialText
        //Log.v(TAG, "Resultado parcial: $partialText")
    }

    /**
     * Cuando ocurre un error en la entrada de voz se indica por logs.
     */
    override fun onError(error: Int) {
        _isListening.value = false
        val errorMessage = getErrorText(error)
        _recognizedText.value = ""
        _recognitionError.value=true

        Log.e(TAG, "Error de reconocimiento: $errorMessage ($error)")
    }



    /**
     * Convierte un código de error en un mensaje de error correspondiente.
     */
    private fun getErrorText(error: Int): String {
        return when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Error de captura de audio."
            SpeechRecognizer.ERROR_CLIENT -> "Error del cliente."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permiso de micrófono no concedido."
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera de red agotado."
            SpeechRecognizer.ERROR_NETWORK -> "Error de red."
            SpeechRecognizer.ERROR_NO_MATCH -> "No se encontró coincidencia de voz."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Servicio de reconocimiento ocupado."
            SpeechRecognizer.ERROR_SERVER -> "Error del servidor."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó entrada de voz."
            else -> "Error desconocido."
        }
    }
    override fun clearText() {
        _recognizedText.value = ""
        _recognitionError.value = false
    }

    // Métodos menos comunes no implementados
    override fun onRmsChanged(rmsdB: Float) { /* Útil para animar una barra de sonido en la UI */ }
    override fun onBufferReceived(buffer: ByteArray?) { /* Ignorado en la mayoría de los casos */ }
    override fun onEvent(eventType: Int, params: Bundle?) { /* Ignorado en la mayoría de los casos */ }
}