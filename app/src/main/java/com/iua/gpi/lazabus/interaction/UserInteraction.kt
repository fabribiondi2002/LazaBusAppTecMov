package com.iua.gpi.lazabus.interaction

import android.util.Log
import com.iua.gpi.lazabus.data.remote.model.RutaOptima
import com.iua.gpi.lazabus.ui.viewmodel.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.Normalizer
import kotlin.math.log

/**
 * Funcion que permite normalizar los textos ingresados por voz.
 */
private fun normalize(text: String): String {
    return Normalizer.normalize(text, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}

private const val SPEACH_WAIT_TIME: Long = 5000
private const val SPEACH_SHORT_WAIT_TIME: Long = 2000
private const val SPEACH_LONG_WAIT_TIME: Long = 7000
private const val TAG = "UserInteraction"

/**
 * Funcion que permite manejar la interaccion principal del usuario.
 */
suspend fun manageInteraction(
    tts: TtsViewModel,
    stt: SttViewModel,
    geocoder: GeocoderViewModel,
    location: LocationViewModel,
    rutas: RutaViewModel,
    buttons: ButtonManagerViewModel,
    viajeVM: ViajeViewModel,
    firstTime: Boolean = true
) {

    if (firstTime) {
        bienvenida(tts, buttons)
    }
    val destino = capturarDestino(tts, stt, buttons)

    procesarDestino(destino, geocoder, location)

    val rutaOptima = calcularRuta(location, rutas)

    anunciarRuta(tts, rutaOptima, destino)

    guardarViaje(viajeVM, rutaOptima, destino, location, buttons)
}

/**
 * Funcion que permite manejar la primera interaccion con una bienvenida al usuario.
 */
private suspend fun bienvenida(
    tts: TtsViewModel,
    buttons: ButtonManagerViewModel
) {
    buttons.updateState(InteractionState.SPEAKING)
    tts.hablar(Dialogos.bienvenida)
    delay(SPEACH_WAIT_TIME)

    buttons.reset()
    buttons.updateState(InteractionState.AWAITING_CONFIRMATION)
    buttons.awaitConfirmation()
}

/**
 * Funcion que permite manejar la captura del destino del usuario.
 * El usuario ingresa el destino por dictado de voz.
 * @return el destino ingresado por el usuario en string.
 */
private suspend fun capturarDestino(
    tts: TtsViewModel,
    stt: SttViewModel,
    buttons: ButtonManagerViewModel
): String {

    buttons.updateState(InteractionState.SPEAKING)
    tts.hablar(Dialogos.pedirDestino)
    delay(SPEACH_SHORT_WAIT_TIME)

    buttons.updateState(InteractionState.LISTENING)
    stt.startVoiceInput()
    delay(SPEACH_WAIT_TIME)
    stt.stopVoiceInput()

    while (stt.recognitionError.value || stt.uiText.value.isBlank()) {
        buttons.updateState(InteractionState.SPEAKING)
        tts.hablar(Dialogos.repetirDestino)
        delay(SPEACH_WAIT_TIME)

        buttons.updateState(InteractionState.LISTENING)
        stt.startVoiceInput()
        delay(SPEACH_WAIT_TIME)
        stt.stopVoiceInput()
    }

    val texto = stt.uiText.value.trim()

    buttons.updateState(InteractionState.SPEAKING)
    tts.hablar(Dialogos.confirmarDestino(texto))
    delay(SPEACH_LONG_WAIT_TIME)

    return texto
}

/**
 * Funcion que obtiene la geolocalizacion del usuario y transforma el destino de texto a geolocalizacion.
 * Guardan la informacion del origen y el destino en el LocationViewModel
 */
private suspend fun procesarDestino(
    destinoTexto: String,
    geocoder: GeocoderViewModel,
    location: LocationViewModel
){

    val origen = location.currentLocation.value
    location.setOrigen(origen?.latitude ?: -31.4126, origen?.longitude ?: -64.2005)

    val limpio = normalize(destinoTexto)
    geocoder.clearLocation()
    geocoder.buscarUbicacion(limpio)

    val destino = geocoder.geocoderState
        .map { it.location }
        .filterNotNull()
        .first()

    Log.i(TAG, "Destino obtenido del geocoder: ${destino.featureName} (${destino.latitude}, ${destino.longitude})")
    location.setDestino(destino.latitude, destino.longitude)
}


/**
 * Funcion que consulta la ruta optima al backend enviandole las coordenadas del punto de origen y el destino.
 * @return la ruta optima encontrada.
 */
private suspend fun calcularRuta(
    location: LocationViewModel,
    rutas: RutaViewModel
): RutaOptima {

    val origen = location.currentLocation
        .filterNotNull()
        .first()

    val destino = location.destinoGeoPoint
        .filterNotNull()
        .first()
    rutas.limpiarRuta()

    rutas.calcularRutaOptima(
        origen.latitude,
        origen.longitude,
        destino.latitude,
        destino.longitude,
    ) { e ->
        Log.e("UserInteraction", "Error calculando ruta: $e")
    }

    return rutas.rutaOptima.filterNotNull().first()
}

/**
 * Funcion que permite anunciar la ruta optima al usuario.
 */
private fun anunciarRuta(
    tts: TtsViewModel,
    ruta: RutaOptima,
    destinoTexto: String,
) {
    val distanciaOrigen = String.format("%.0f", ruta.distanciaOrigen * 1000)
    val distanciaDestino = String.format("%.0f", ruta.distanciaDestino * 1000)

    val mensaje = Dialogos.anunciarRuta(
        ruta = ruta.ruta.nombre,
        empresa = ruta.ruta.descripcion,
        distanciaOrigen = distanciaOrigen,
        inicio = ruta.paradaOrigen.nombre,
        final = ruta.paradaDestino.nombre,
        distanciaDestino = distanciaDestino,
        destinoTexto = destinoTexto
    )
    tts.hablar(mensaje)

}

/**
 * Funcion que permite guardar el viaje realizado por el usuario en la base de datos local.
 */
private fun guardarViaje(
    viajeVM: ViajeViewModel,
    ruta: RutaOptima,
    destinoTexto: String,
    location: LocationViewModel,
    buttons: ButtonManagerViewModel

) {
    val origen = location.currentLocation.value

    viajeVM.agregarViaje(
        ruta = ruta.ruta.nombre,
        descripcionRuta = ruta.ruta.descripcion,
        origen = "Lat: ${origen?.latitude}, Lon: ${origen?.longitude}",
        destino = destinoTexto,
        paradaOrigen = ruta.paradaOrigen.nombre,
        paradaDestino = ruta.paradaDestino.nombre
    )
    buttons.updateState(InteractionState.AWAITING_RESTART_CONFIRMATION)

}

/**
 * Funcion que permite reiniciar el flujo para una nueva consulta.
 */
suspend fun restartInteraction(
    tts: TtsViewModel,
    stt: SttViewModel,
    geocoder: GeocoderViewModel,
    location: LocationViewModel,
    rutas: RutaViewModel,
    buttons: ButtonManagerViewModel,
    viajeVM: ViajeViewModel
) {
    rutas.limpiarRuta()
    geocoder.clearLocation()
    location.clearDestino()
    location.clearOrigen()
    stt.clearText()
    buttons.reset()

    manageInteraction(
        tts = tts,
        stt = stt,
        geocoder = geocoder,
        location = location,
        rutas = rutas,
        buttons = buttons,
        viajeVM = viajeVM,
        firstTime = false
    )
}
