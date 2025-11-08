package com.iua.gpi.lazabus.interaction

import com.iua.gpi.lazabus.ui.viewmodel.GeocoderViewModel
import com.iua.gpi.lazabus.ui.viewmodel.LocationViewModel
import com.iua.gpi.lazabus.ui.viewmodel.RutaViewModel
import com.iua.gpi.lazabus.ui.viewmodel.SttViewModel
import com.iua.gpi.lazabus.ui.viewmodel.TtsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val SPEACH_WAIT_TIME : Long = 5000

suspend fun manageInteraction(
    ttsviewModel: TtsViewModel,
    sttviewmodel: SttViewModel,
    geocoderViewModel: GeocoderViewModel,
    locationViewModel: LocationViewModel,
    rutaViewModel: RutaViewModel
)
{
    /*
    olat=-31.412684894741222
    olng=-64.20055023585927
    dlat= -31.4328235977395
    dlng= -64.27699975384215
     */
    rutaViewModel.calcularRutaOptima(-31.412684894741222,-64.20055023585927,-31.4328235977395, -64.27699975384215,{})


/*
    ttsviewModel.hablar("Bienvenido a Laza Bus... ¿A dónde te interesaría ir hoy?")
    delay(SPEACH_WAIT_TIME)
    sttviewmodel.startVoiceInput()
    delay(SPEACH_WAIT_TIME)
    sttviewmodel.stopVoiceInput()
    ttsviewModel.hablar("Perfecto, entendí que dijiste ..." + sttviewmodel.uiText.value)

    geocoderViewModel.buscarUbicacion(sttviewmodel.uiText.value)
*/


}