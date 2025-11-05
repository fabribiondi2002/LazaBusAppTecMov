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

class SttService (private val context: Context
) : SttServiceI, RecognitionListener {

    private val TAG = "SttService"

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    // Flows para exponer el estado a la capa superior
    private val _recognizedText = MutableStateFlow("")
    override val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    override val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    init {
        speechRecognizer.setRecognitionListener(this)
    }

    override fun startListening() {
        // Lógica de Intent y startListening aquí...
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM // Dictado general, no comandos específicos
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechRecognizer.startListening(intent)
        _isListening.value = true
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
        _isListening.value = false
    }


    // Implementación de RecognitionListener
    // Cuando el servicio está listo para recibir voz.
    override fun onReadyForSpeech(params: Bundle?) {
        _recognizedText.value = "" // Limpiamos el texto anterior al comenzar una nueva sesión
        Log.d(TAG, "Micrófono listo para hablar.")
    }

    // Cuando el usuario comienza a hablar. Útil para feedback visual.
    override fun onBeginningOfSpeech() {
        Log.d(TAG, "Comienzo de la entrada de voz.")
    }

    //Cuando el usuario deja de hablar. El procesamiento comienza.
    override fun onEndOfSpeech() {
        Log.d(TAG, "Fin de la entrada de voz, esperando resultados.")
    }

    // Se recibe el resultado del reconocimiento (Callback más importante).
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

    // Ocurre un error durante el proceso de reconocimiento.
    override fun onError(error: Int) {
        _isListening.value = false // Detenemos la escucha debido al error
        val errorMessage = getErrorText(error)

        // Notificamos el error al ViewModel o UI (puedes usar otro StateFlow para errores)
        _recognizedText.value = "Error: $errorMessage"
        Log.e(TAG, "Error de reconocimiento: $errorMessage ($error)")
    }

    // Se detecta un cambio en el volumen (Root Mean Square - RMS).
    override fun onRmsChanged(rmsdB: Float) {
        // Útil para animar una barra de sonido en la UI
        //Log.v(TAG, "Volumen cambiado (RMS): $rmsdB dB")
    }

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

    // Métodos menos comunes que se deben implementar
    override fun onBufferReceived(buffer: ByteArray?) { /* Ignorado en la mayoría de los casos */ }
    override fun onEvent(eventType: Int, params: Bundle?) { /* Ignorado en la mayoría de los casos */ }

}