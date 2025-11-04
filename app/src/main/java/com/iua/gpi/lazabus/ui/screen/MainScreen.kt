package com.iua.gpi.lazabus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.iua.gpi.lazabus.ui.viewmodel.TtsViewModel

// Definimos los colores principales para mantener la coherencia con el diseño de la captura
val LazabusBlue = Color(0xFF1E88E5) // Un azul brillante para el app bar y el botón

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen( viewModel: TtsViewModel = hiltViewModel()) {

    viewModel.saludar()
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = topPadding)
                    .background(Color(0xFFF0F0F0)) // Fondo ligero para el resto de la pantalla
            ) {
                DestinoArea(destino = "Plaza San Martin") //todo: ver como integrar bien
                // Área del Mapa (grande con desplazamiento)
                MapArea(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                    .fillMaxWidth()
                    .background(LazabusBlue)
                ) {   // Botón Grande de Comandos de Voz (parte inferior)
                    VoiceActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom =  paddingValues.calculateBottomPadding()),
                        onClick = { viewModel.saludar() }
                    )

                }
            }
        }
    )
}




