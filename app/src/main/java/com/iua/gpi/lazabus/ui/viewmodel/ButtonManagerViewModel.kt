package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class InteractionState {
    IDLE,               // Inicial, inactivo o terminado
    SPEAKING,           // El sistema está hablando (ej: "Bienvenido...")
    LISTENING,          // El sistema está escuchando la respuesta del usuario
    AWAITING_CONFIRMATION, // El flujo está pausado, esperando el clic del botón (ej: "¿Es correcto?")
    PROCESSING,          // El sistema está buscando/calculando la ruta
    AWAITING_RESTART_CONFIRMATION // El sistema está esperando la confirmación de reiniciar el flujo
}

@HiltViewModel
class ButtonManagerViewModel @Inject constructor() : ViewModel() {

    // STATEFLOW principal para que la UI observe el estado.
    private val _state = MutableStateFlow(InteractionState.IDLE)
    val state: StateFlow<InteractionState> = _state

    // Mecanismo de suspensión: La promesa de la confirmación.
    private var confirmationDeferred: CompletableDeferred<Boolean>? = null

    // Método para que manageInteraction actualice el estado del flujo.
    fun updateState(newState: InteractionState) {
        _state.value = newState
    }

    // El método suspendido que PAUSA la corrutina de manageInteraction.
    suspend fun awaitConfirmation(state: InteractionState = InteractionState.AWAITING_CONFIRMATION): Boolean {
        // Prepara la promesa
        val deferred = CompletableDeferred<Boolean>()
        confirmationDeferred = deferred

        // Setea el estado para la UI (cambia el ícono a "Check")
        _state.value = state
        // SUSPENDE la corrutina hasta que se presione el botón
        val result = deferred.await()

        // 4. Limpia el mecanismo
        confirmationDeferred = null
        return result
    }

    // FUNCIÓN DE ACCIÓN DEL BOTÓN SIMPLE: Solo desactiva la espera.
    fun confirmInteraction() {
        // Completa la promesa con TRUE para reanudar manageInteraction
        confirmationDeferred?.complete(true)
    }
    fun reset() {
        confirmationDeferred?.cancel()
        confirmationDeferred = null
        _state.value = InteractionState.IDLE
    }


}