package com.iua.gpi.lazabus.ui.permission

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/**
 * Componente Composable que solicita automáticamente el permiso RECORD_AUDIO al cargarse.
 * NO renderiza ninguna UI, solo dispara el diálogo del sistema operativo.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MicPermissionRequest(
    // Callback opcional: se ejecuta después de que el usuario haya respondido
    onPermissionResult: (Boolean) -> Unit = {}
) {
    // Rastrea el estado del permiso de micrófono
    val micPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )

    // 2. Dispara el efecto de solicitud de permiso automáticamente al inicio
    LaunchedEffect(micPermissionState.status) {

        // La clave de este LaunchedEffect es micPermissionState.status:
        // El bloque se ejecuta cuando el Composable inicia y cada vez que el estado del permiso cambia.

        if (!micPermissionState.status.isGranted) {
            // Si el permiso no está concedido, lanzamos el pop-up del sistema.
            micPermissionState.launchPermissionRequest()
        }

        // 3. Informa el resultado al componente padre (MainScreen)
        // Esto se llama después de que el diálogo del sistema se ha resuelto.
        onPermissionResult(micPermissionState.status.isGranted)
    }

    // Este Composable no retorna ningún elemento de UI (como Box, Text, etc.). Es invisible.
}