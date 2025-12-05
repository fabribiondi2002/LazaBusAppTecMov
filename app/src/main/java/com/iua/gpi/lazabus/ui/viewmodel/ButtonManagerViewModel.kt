package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Estados de interacción con el usuario.
 */
enum class InteractionState {
    IDLE,                       // Inicial / finalizado
    SPEAKING,                   // TTS hablando
    LISTENING,                  // STT escuchando
    AWAITING_CONFIRMATION,      // Espera confirmación de usuario
    PROCESSING,                 // Cálculo o trabajo pesado
    AWAITING_RESTART_CONFIRMATION // Espera confirmar reinicio
}

/**
 * ViewModel que maneja el estado de interacción con el usuario.
 */
@HiltViewModel
class ButtonManagerViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(InteractionState.IDLE)
    val state: StateFlow<InteractionState> = _state

    private var confirmationDeferred: CompletableDeferred<Boolean>? = null

    // 🔥 Callback para reinicio
    private var onRestart: suspend () -> Unit = {}

    fun setOnRestartCallback(callback: suspend () -> Unit) {
        onRestart = callback
    }

    fun updateState(newState: InteractionState) {
        _state.value = newState
    }

    /**
     * Suspende el flujo hasta que el usuario toque el botón.
     */
    suspend fun awaitConfirmation(
        state: InteractionState = InteractionState.AWAITING_CONFIRMATION
    ): Boolean {

        val deferred = CompletableDeferred<Boolean>()
        confirmationDeferred = deferred

        _state.value = state

        val result = deferred.await()

        confirmationDeferred = null
        return result
    }

    /**
     * El usuario presionó el botón.
     */
    fun confirmInteraction() {
        when (_state.value) {

            InteractionState.AWAITING_CONFIRMATION -> {
                confirmationDeferred?.complete(true)
            }

            InteractionState.AWAITING_RESTART_CONFIRMATION -> {
                // Cuando se confirma reinicio -> completar y correr callback
                confirmationDeferred?.complete(true)
            }

            else -> {
                // Cualquier otro estado -> ignorar
            }
        }
    }

    /**
     * El usuario solicitó explícitamente reiniciar
     * (se usa cuando tocás el botón "Nueva consulta")
     */
    fun requestRestart() {
        _state.value = InteractionState.AWAITING_RESTART_CONFIRMATION
    }

    /**
     * Se llama desde MainScreen:
     * buttons.setOnRestartCallback { … }
     */
    suspend fun executeRestartCallback() {
        onRestart()
    }

    fun reset() {
        confirmationDeferred?.cancel()
        confirmationDeferred = null
        _state.value = InteractionState.IDLE
    }
}
