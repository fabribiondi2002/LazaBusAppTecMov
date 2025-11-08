package com.iua.gpi.lazabus.ui.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.Manifest
import com.google.accompanist.permissions.isGranted

/**
 * Componente Composable que solicita automáticamente los permisos de ubicación al cargarse.
 * NO renderiza ninguna UI, solo dispara el diálogo del sistema operativo.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequest(
    // Callback opcional: se ejecuta después de que el usuario haya respondido o si ya están concedidos.
    // El booleano será 'true' si AL MENOS uno de los permisos requeridos (FINE o COARSE) fue concedido.
    onPermissionResult: (Boolean) -> Unit = {}
) {
    // Rastrea el estado de los permisos de ubicación
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // 2. Dispara el efecto de solicitud de permiso automáticamente al inicio o cuando el estado cambie
    LaunchedEffect(locationPermissionsState.allPermissionsGranted, locationPermissionsState.revokedPermissions.size) {

        // La clave de este LaunchedEffect es el estado de los permisos:
        // El bloque se ejecuta cuando el Composable inicia y cada vez que el estado de los permisos cambia.

        // Comprobamos si *al menos* uno de los permisos necesarios (FINE o COARSE) está concedido.
        // Los permisos se consideran concedidos si la lista de permisos revocados es menor a la cantidad
        // de permisos totales, o si 'allPermissionsGranted' es true (aunque para ubicación solo necesites uno).
        val isAtLeastOneGranted = locationPermissionsState.permissions.any { it.status.isGranted }

        if (!isAtLeastOneGranted) {
            // Si ninguno está concedido, lanzamos el pop-up del sistema.
            // Esto solo se lanza si el usuario no ha negado permanentemente (shouldShowRationale no está incluido aquí para simplificar,
            // pero podrías añadir lógica para mostrar un 'Rationale' antes de solicitar).
            locationPermissionsState.launchMultiplePermissionRequest()
        }

        // 3. Informa el resultado al componente padre
        // Esto se llama después de que el diálogo del sistema se ha resuelto.
        onPermissionResult(isAtLeastOneGranted)
    }

    // Este Composable no retorna ningún elemento de UI (como Box, Text, etc.). Es invisible.
}