package com.iua.gpi.lazabus.interaction

import android.util.Log
import com.iua.gpi.lazabus.ui.viewmodel.ButtonManagerViewModel
import com.iua.gpi.lazabus.ui.viewmodel.GeocoderViewModel
import com.iua.gpi.lazabus.ui.viewmodel.InteractionState
import com.iua.gpi.lazabus.ui.viewmodel.LocationViewModel
import com.iua.gpi.lazabus.ui.viewmodel.RutaViewModel
import com.iua.gpi.lazabus.ui.viewmodel.SttViewModel
import com.iua.gpi.lazabus.ui.viewmodel.TtsViewModel
import kotlinx.coroutines.delay

val SPEACH_WAIT_TIME : Long = 5000
val SPEACH_SHORT_WAIT_TIME : Long = 2000
val SPEACH_LONG_WAIT_TIME: Long = 7000
val TAG = "UserInteraction"
suspend fun manageInteraction(
    ttsviewModel: TtsViewModel,
    sttviewmodel: SttViewModel,
    geocoderViewModel: GeocoderViewModel,
    locationViewModel: LocationViewModel,
    rutaViewModel: RutaViewModel,
    buttonManagerViewModel: ButtonManagerViewModel
)
{
    buttonManagerViewModel.updateState(InteractionState.SPEAKING)
    ttsviewModel.hablar("Bienvenido a Laza Bus, presiona la parte inferior de tu pantalla para comenzar")
    buttonManagerViewModel.updateState(InteractionState.AWAITING_CONFIRMATION)
    delay(SPEACH_WAIT_TIME)

    buttonManagerViewModel.awaitConfirmation()

    buttonManagerViewModel.updateState(InteractionState.SPEAKING)
    ttsviewModel.hablar("¿A dónde te interesaría ir hoy?")
    delay(SPEACH_SHORT_WAIT_TIME)

    buttonManagerViewModel.updateState(InteractionState.LISTENING)
    sttviewmodel.startVoiceInput()
    delay(SPEACH_WAIT_TIME)
    sttviewmodel.stopVoiceInput()

    while(sttviewmodel.recognitionError.value || sttviewmodel.uiText.value == "")
    {
        buttonManagerViewModel.updateState(InteractionState.SPEAKING)
        ttsviewModel.hablar("Disculpa, no pude entenderte bien. ¿Podrías volver a decirlo?")
        delay(SPEACH_WAIT_TIME)
        buttonManagerViewModel.updateState(InteractionState.LISTENING)
        sttviewmodel.startVoiceInput()
        delay(SPEACH_WAIT_TIME)
        sttviewmodel.stopVoiceInput()
    }

    buttonManagerViewModel.updateState(InteractionState.SPEAKING)

    ttsviewModel.hablar("Perfecto, entendí que te interesa ir a " + sttviewmodel.uiText.value)
    delay(SPEACH_LONG_WAIT_TIME)

    buttonManagerViewModel.updateState(InteractionState.PROCESSING)
    ttsviewModel.hablar("Buscando la mejor ruta a tu destino")
    delay(SPEACH_WAIT_TIME)

    geocoderViewModel.buscarUbicacion(sttviewmodel.uiText.value)

    rutaViewModel.calcularRutaOptima(-31.412684894741222,-64.20055023585927,-31.4328235977395, -64.27699975384215,
        { e ->
            Log.e(TAG, "Fallo calculo de ruta error: " + e)
        })
    Log.i(TAG, "Ruta a indicar: " + rutaViewModel.rutaOptima.value?.ruta?.nombre)
    ttsviewModel.hablar("""La mejor ruta, en base a tu ubicación actual y el destino deseado es 
        ${rutaViewModel.rutaOptima.value?.ruta?.nombre}
    """)

}