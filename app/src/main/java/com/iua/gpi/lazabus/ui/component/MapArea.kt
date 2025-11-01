package com.iua.gpi.lazabus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import com.iua.gpi.lazabus.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun MapArea(modifier: Modifier, mapDescription: String = stringResource(R.string.mapDescription)) {
    Box(
        modifier = modifier
            .background(Color.LightGray) // Color base para el mapa
            .semantics { contentDescription = mapDescription }
    ) {
        val context = LocalContext.current

        // Configurar osmdroid al inicio (solo una vez)
        // cache del dispositivo.
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

        //Crear y recordar la instancia de MapView
        val mapView = remember {
            MapView(context).apply {
                // Establecer la fuente de los tiles (OSM estándar)
                setTileSource(TileSourceFactory.MAPNIK)
                setBuiltInZoomControls(false)
                setMultiTouchControls(true)

                // Establecer la posición inicial
                val cordoba = GeoPoint(-31.428447981733132, -64.18492561421215)
                controller.setZoom(16.0)
                controller.setCenter(cordoba)

                // Agregar un marcador (ejemplo simplificado)
                // val marker = Marker(this)
                // marker.position = madrid
                // marker.title = "Madrid"
                // overlays.add(marker)
            }
        }

        //Usar AndroidView para incrustar el MapView de osmdroid en Compose
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView },
            update = { /* Puedes actualizar propiedades del mapa aquí si es necesario */ }
        )
    }
}