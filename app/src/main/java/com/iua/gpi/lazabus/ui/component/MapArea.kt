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
/**
 * Composable del mapa de la aplicación.
 */
@Composable
fun MapArea(
    modifier: Modifier,
    mapDescription: String = stringResource(R.string.mapDescription),
    onMapReady: (MapView) -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(Color.LightGray) // Color base para el mapa
            .semantics { contentDescription = mapDescription }
    ) {
        val context = LocalContext.current

        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))

        val mapView = remember {
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setBuiltInZoomControls(false)
                setMultiTouchControls(true)
                val cordoba = GeoPoint(-31.428447981733132, -64.18492561421215)
                controller.setZoom(16.0)
                controller.setCenter(cordoba)
            }
        }
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { mapView },
            update = {onMapReady(mapView) }
        )
    }
}