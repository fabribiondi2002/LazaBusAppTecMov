package com.iua.gpi.lazabus.ui.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iua.gpi.lazabus.R
import com.iua.gpi.lazabus.interaction.manageInteraction
import com.iua.gpi.lazabus.interaction.restartInteraction
import com.iua.gpi.lazabus.ui.component.MapArea
import com.iua.gpi.lazabus.ui.component.MapMarkers
import com.iua.gpi.lazabus.ui.component.VoiceActionButton
import com.iua.gpi.lazabus.ui.permission.LocationPermissionRequest
import com.iua.gpi.lazabus.ui.permission.MicPermissionRequest
import com.iua.gpi.lazabus.ui.viewmodel.*
import kotlinx.coroutines.launch
import org.osmdroid.views.MapView
import com.iua.gpi.lazabus.ui.theme.LazabusBlue
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Composable de la pantalla principal
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onOpenHistorial: () -> Unit,
    ttsviewModel: TtsViewModel = hiltViewModel(),
    sttviewmodel: SttViewModel = hiltViewModel(),
    geocoderViewModel: GeocoderViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    rutaViewModel: RutaViewModel = hiltViewModel(),
    buttonManagerViewModel : ButtonManagerViewModel = hiltViewModel(),
    viajeViewModel: ViajeViewModel = hiltViewModel()
) {

    // PERMISOS
    MicPermissionRequest()
    LocationPermissionRequest()

    val paradasMapa by rutaViewModel.paradasGeoPoints.collectAsState()
    val origen = locationViewModel.origenGeoPoint.collectAsState().value
    val destino = locationViewModel.destinoGeoPoint.collectAsState().value
    val currentInteractionState by buttonManagerViewModel.state.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        manageInteraction(
            ttsviewModel,
            sttviewmodel,
            geocoderViewModel,
            locationViewModel,
            rutaViewModel,
            buttonManagerViewModel,
            viajeViewModel,
        )

    }
    DisposableEffect(Unit) {
        onDispose {
            rutaViewModel.finalizarViaje()
            locationViewModel.clearLocation()

        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.menu_title),
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(
                        onClick = {
                            scope.launch { drawerState.close() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.menu_close)
                        )
                    }
                }

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_history)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onOpenHistorial()
                    },
                    icon = {
                        Icon(Icons.Default.DateRange, contentDescription = stringResource(R.string.menu_history))
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                )

                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.menu_contact)) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        contactarPorTelefono(context)
                    },
                    icon = {
                        Icon(Icons.Default.Call, contentDescription = stringResource(R.string.menu_contact))
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                )
                Text(
                    stringResource(R.string.voice_settings),
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                val speed by ttsviewModel.speed.collectAsState()

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(stringResource(R.string.reading_speed, speed))

                    Slider(
                        value = speed,
                        onValueChange = { newSpeed ->
                            ttsviewModel.updateSpeed(newSpeed)
                        },
                        valueRange = 0.5f..1.5f,
                        steps = 10
                    )
                }

                Text(
                    stringResource(R.string.voice_language),
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                val currentLang by ttsviewModel.language.collectAsState()

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLang == "es",
                        onClick = { ttsviewModel.updateLanguage("es") }
                    )
                    Text(stringResource(R.string.spanish))

                    Spacer(Modifier.width(16.dp))

                    RadioButton(
                        selected = currentLang == "en",
                        onClick = { ttsviewModel.updateLanguage("en") }
                    )
                    Text(stringResource(R.string.english))
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    ) {

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
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(R.string.menu_title)
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
                        .background(Color(0xFFF0F0F0))
                ) {

                    // Mapa
                    MapArea(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        onMapReady = { mapViewState.value = it }
                    )

                    MapMarkers(
                        mapView = mapViewState.value,
                        coordinates = paradasMapa,
                        origen = origen,
                        destino = destino
                    )

                    // Botón principal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LazabusBlue)
                    ) {
                        val icon: ImageVector = when (currentInteractionState) {
                            InteractionState.AWAITING_CONFIRMATION -> Icons.Filled.PlayArrow
                            InteractionState.PROCESSING -> Icons.Filled.Search
                            InteractionState.SPEAKING -> Icons.Filled.Face
                            InteractionState.LISTENING -> Icons.Filled.AccountCircle
                            InteractionState.AWAITING_RESTART_CONFIRMATION -> Icons.Filled.Refresh
                            else -> Icons.Filled.Warning
                        }

                        VoiceActionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = paddingValues.calculateBottomPadding()),
                            onClick = {
                                vibrar(context)

                                scope.launch {
                                    when (currentInteractionState) {

                                        InteractionState.AWAITING_RESTART_CONFIRMATION -> {
                                            locationViewModel.clearDestino()
                                            rutaViewModel.limpiarRuta()
                                            sttviewmodel.clearText()

                                            restartInteraction(
                                                tts = ttsviewModel,
                                                stt = sttviewmodel,
                                                geocoder = geocoderViewModel,
                                                location = locationViewModel,
                                                rutas = rutaViewModel,
                                                buttons = buttonManagerViewModel,
                                                viajeVM = viajeViewModel
                                            )
                                        }

                                        else -> {
                                            buttonManagerViewModel.confirmInteraction()
                                        }
                                    }
                                }
                            }
                            ,
                            imageVector = icon
                        )
                    }
                }
            }
        )
    }
}

fun vibrar(context : Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(100)
    }
}
fun contactarPorTelefono(context: Context) {
    val numero = "5492920361887"
    val uri = Uri.parse("tel:$numero")

    try {
        val intent = Intent(Intent.ACTION_DIAL, uri)

        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.contact_failed), Toast.LENGTH_SHORT).show()
    }
}

