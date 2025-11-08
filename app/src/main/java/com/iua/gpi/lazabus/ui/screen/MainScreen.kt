package com.iua.gpi.lazabus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.collectAsState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.iua.gpi.lazabus.ui.component.VoiceActionButton
import com.iua.gpi.lazabus.R
import com.iua.gpi.lazabus.ui.component.DestinoArea
import com.iua.gpi.lazabus.ui.component.MapArea
import com.iua.gpi.lazabus.ui.component.MapMarkers
import com.iua.gpi.lazabus.ui.permission.MicPermissionRequest
import com.iua.gpi.lazabus.ui.viewmodel.SttViewModel
import androidx.compose.runtime.getValue
import com.iua.gpi.lazabus.interaction.manageInteraction
import com.iua.gpi.lazabus.ui.permission.LocationPermissionRequest
import com.iua.gpi.lazabus.ui.viewmodel.GeocoderViewModel
import com.iua.gpi.lazabus.ui.viewmodel.LocationViewModel
import com.iua.gpi.lazabus.ui.viewmodel.RutaViewModel
import com.iua.gpi.lazabus.ui.viewmodel.TtsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView

// Definimos los colores principales para mantener la coherencia con el diseño de la captura
val LazabusBlue = Color(0xFF1E88E5) // Un azul brillante para el app bar y el botón

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen( ttsviewModel: TtsViewModel = hiltViewModel(),
                sttviewmodel: SttViewModel = hiltViewModel(),
                geocoderViewModel: GeocoderViewModel = hiltViewModel(),
                locationViewModel: LocationViewModel = hiltViewModel(),
                rutaViewModel: RutaViewModel = hiltViewModel()) {

    //permisos
    MicPermissionRequest()
    LocationPermissionRequest()

    val paradasMapa by rutaViewModel.paradasGeoPoints.collectAsState()

    LaunchedEffect(Unit) {
        manageInteraction(ttsviewModel,sttviewmodel,geocoderViewModel,locationViewModel,rutaViewModel)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.logo_white),
                            contentDescription = "Icono de Autobús",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.app_name),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LazabusBlue)
            )
        },
        content = { paddingValues ->
            val topPadding = paddingValues.calculateTopPadding()

            val mapViewState = remember { mutableStateOf<MapView?>(null) }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding)
                    .background(Color(0xFFF0F0F0)) // Fondo ligero para el resto de la pantalla
            ) {


                // Área del Mapa (grande con desplazamiento)
                MapArea(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    onMapReady = { mapViewState.value = it }
                )

                MapMarkers(
                    mapView = mapViewState.value,
                    coordinates = paradasMapa
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LazabusBlue)
                ) {
                    DestinoArea(
                        destino = "Plaza San Martín",
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                Box(
                    modifier = Modifier
                    .fillMaxWidth()
                    .background(LazabusBlue)
                ) {   // Botón Grande de Comandos de Voz (parte inferior)
                    VoiceActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom =  paddingValues.calculateBottomPadding()),
                        onClick = {    rutaViewModel.calcularRutaOptima(-31.412684894741222,-64.20055023585927,-31.4328235977395, -64.27699975384215,{})
                        }
                    )

                }
            }
        }
    )
}


