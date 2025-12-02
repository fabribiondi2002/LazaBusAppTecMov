package com.iua.gpi.lazabus.interaction

import android.util.Log
import com.iua.gpi.lazabus.ui.viewmodel.ButtonManagerViewModel
import com.iua.gpi.lazabus.ui.viewmodel.GeocoderViewModel
import com.iua.gpi.lazabus.ui.viewmodel.InteractionState
import com.iua.gpi.lazabus.ui.viewmodel.LocationViewModel
import com.iua.gpi.lazabus.ui.viewmodel.RutaViewModel
import com.iua.gpi.lazabus.ui.viewmodel.SttViewModel
import com.iua.gpi.lazabus.ui.viewmodel.TtsViewModel
import com.iua.gpi.lazabus.ui.viewmodel.ViajeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import java.text.Normalizer

private fun normalize(text: String): String {
    return Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}

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
    buttonManagerViewModel: ButtonManagerViewModel,
    viajeViewModel: ViajeViewModel,
    isRestart: Boolean = false

)
{



    if (!isRestart) {
        buttonManagerViewModel.updateState(InteractionState.SPEAKING)
        ttsviewModel.hablar("Bienvenido a Laza Bus, presiona la parte inferior de tu pantalla para comenzar")
        delay(SPEACH_WAIT_TIME)
        buttonManagerViewModel.reset()
        buttonManagerViewModel.updateState(InteractionState.AWAITING_CONFIRMATION)
        buttonManagerViewModel.awaitConfirmation()

    }
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
    val textoDestino = sttviewmodel.uiText.value.trim()
    ttsviewModel.hablar("Perfecto, entendí que te interesa ir a " + sttviewmodel.uiText.value)
    Log.i(TAG,"destino " + sttviewmodel.uiText.value)
    delay(SPEACH_LONG_WAIT_TIME)

    buttonManagerViewModel.updateState(InteractionState.PROCESSING)
    ttsviewModel.hablar("Buscando la mejor ruta a tu destino")
    delay(SPEACH_WAIT_TIME)

    val origen = locationViewModel.currentLocation.value
    Log.i(TAG," origen " + origen?.latitude)
    locationViewModel.setOrigen(origen?.latitude ?: -31.4126, origen?.longitude ?: -64.2005,)

    val textoDestinoLimpio = normalize(textoDestino)
    Log.i(TAG, "texto limpio" + textoDestinoLimpio)
    geocoderViewModel.buscarUbicacion(textoDestinoLimpio)



    var destino = geocoderViewModel.geocoderState.value.location
    while(destino == null)
    {
        delay(100)
        destino = geocoderViewModel.geocoderState.value.location
        Log.i(TAG," no se encontro destino ")
    }
    locationViewModel.setDestino(destino.latitude, destino.longitude)

    rutaViewModel.calcularRutaOptima(
        origen?.latitude ?: -31.4126,
        origen?.longitude ?: -64.2005,
        destino.latitude,
        destino.longitude,
        { e ->
            Log.e(TAG, "Fallo calculo de ruta error: $e")
        }
    )

    val rutaOptima = rutaViewModel.rutaOptima
        .filterNotNull()
        .first()

    val nombreRuta = rutaOptima.ruta.nombre
    val empresaRuta = rutaOptima.ruta.descripcion
    val paradaOrigen= rutaOptima.paradaOrigen.nombre
    val distanciaOrigen = rutaOptima.distanciaOrigen
    val paradaDestino = rutaOptima.paradaDestino.nombre
    val distanciaDestino = rutaOptima.distanciaDestino

    viajeViewModel.agregarViaje(
        ruta = nombreRuta,
        descripcionRuta = empresaRuta,
        origen = "Lat: " + origen?.latitude.toString() + ", Lon: " + origen?.longitude.toString(),
        destino = textoDestino,
        paradaOrigen = paradaOrigen,
        paradaDestino = paradaDestino
    )
    buttonManagerViewModel.updateState(InteractionState.SPEAKING)
    Log.i(TAG,"La mejor ruta, en base a tu ubicación actual y el destino deseado es la ruta $nombreRuta de la empresa $empresaRuta, la parada mas cercana a tu ubicacion esta a $distanciaOrigen metros en $paradaOrigen, te tendras que bajar en la parada $paradaDestino que esta a $distanciaDestino metros de $textoDestino")
    ttsviewModel.hablar(
        "La mejor ruta, en base a tu ubicación actual y el destino deseado es la ruta ${nombreRuta}" +
                " de la empresa $empresaRuta, la parada mas cercana a tu ubicacion esta a" + String.format("%.0f", distanciaOrigen * 1000) +
                " metros en $paradaOrigen, te tendras que bajar en la parada $paradaDestino que esta a " +
                String.format("%.0f", distanciaDestino * 1000) + " metros de $textoDestino. Si desea realizar una nueva consulta presione la parte inferior para comenzar de nuevo"
    )

    delay(SPEACH_LONG_WAIT_TIME)
    buttonManagerViewModel.updateState(InteractionState.AWAITING_RESTART_CONFIRMATION)
// Cambiamos a estado de reinicio

// Esperamos a que el usuario toque el botón
    val restart = buttonManagerViewModel.awaitConfirmation( InteractionState.AWAITING_RESTART_CONFIRMATION)

    if (restart) {
        sttviewmodel.reset()
        rutaViewModel.finalizarViaje()
        locationViewModel.clearLocation()
        Log.i(TAG, "Usuario reinició la interacción")
        return
    }


}