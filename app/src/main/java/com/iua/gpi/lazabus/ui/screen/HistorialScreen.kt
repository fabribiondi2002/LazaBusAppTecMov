package com.iua.gpi.lazabus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iua.gpi.lazabus.R
import com.iua.gpi.lazabus.ui.viewmodel.ViajeViewModel
import com.iua.gpi.lazabus.ui.theme.LazabusBlue

/**
 * Composable de la pantalla de historial de viajes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    onBack: () -> Unit,
    viajeViewModel: ViajeViewModel = hiltViewModel()
) {
    val historial by viajeViewModel.historial.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.menu_history), color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viajeViewModel.limpiarHistorial() }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.clear_history),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LazabusBlue
                )
            )
        }
    ) { padding ->

        if (historial.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_history),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF2F2F2))
            ) {
                items(historial) { viaje ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LazabusBlue
                        ),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = viaje.ruta.uppercase(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = viaje.descripcionRuta,
                                fontSize = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White.copy(alpha = 0.9f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${stringResource(R.string.origin)}: ${viaje.origen}",
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                            Text(
                                text = "${stringResource(R.string.destination)}: ${viaje.destino}",
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${stringResource(R.string.origin_stop)}: ${viaje.paradaOrigen}",
                                color = Color.White
                            )
                            Text(
                                text = "${stringResource(R.string.destination_stop)}: ${viaje.paradaDestino}",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
