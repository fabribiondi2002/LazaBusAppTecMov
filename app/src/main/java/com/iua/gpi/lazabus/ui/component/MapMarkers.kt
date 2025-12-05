package com.iua.gpi.lazabus.ui.component

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import androidx.core.graphics.toColorInt

/**
 * Overlay personalizado que dibuja puntos (círculos) en el mapa.
 */
class PointsOverlay(
    private val points: List<GeoPoint>,
    private val radiusPx: Float = 10f,
    private val fillColor: Int = Color.RED,
    private val strokeColor: Int = Color.WHITE,
    private val strokeWidthPx: Float = 2f
) : Overlay() {

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = fillColor
    }

    private val strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        color = strokeColor
        strokeWidth = strokeWidthPx
    }

    private val tmpPoint = Point()

    /**
     * Dibuja los puntos en el mapa.
     */
    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        if (shadow) return

        val projection = mapView.projection
        for (geo in points) {
            projection.toPixels(geo, tmpPoint)
            val x = tmpPoint.x.toFloat()
            val y = tmpPoint.y.toFloat()
            canvas.drawCircle(x, y, radiusPx, fillPaint)
            if (strokeWidthPx > 0f) {
                canvas.drawCircle(x, y, radiusPx, strokePaint)
            }
        }
    }
}

/**
 * Composable que agrega puntos (círculos) al MapView pasado.
 */
@Composable
fun MapMarkers(
    mapView: MapView?,
    coordinates: List<GeoPoint>,
    origen: GeoPoint?,
    destino: GeoPoint?,
    radiusPx: Float = 20f,
    strokeColor: Int = Color.WHITE,
    strokeWidthPx: Float = 2f
) {
    val colorRuta = "#0072B2".toColorInt()     // Azul
    val colorOrigen = "#E69F00".toColorInt()   // Naranja
    val colorDestino = "#CC79A7".toColorInt()  // Magenta
    LaunchedEffect(mapView, coordinates, origen, destino) {

        if (mapView == null) return@LaunchedEffect

        delay(200)

        val toRemove = mapView.overlays.filterIsInstance<PointsOverlay>()
        mapView.overlays.removeAll(toRemove)

        // ================================
        //    OVERLAY DE LA RUTA
        // ================================
        if (coordinates.isNotEmpty()) {
            val pointsOverlay = PointsOverlay(
                points = coordinates,
                radiusPx = radiusPx,
                fillColor = colorRuta,
                strokeColor = strokeColor,
                strokeWidthPx = strokeWidthPx
            )
            mapView.overlays.add(pointsOverlay)

            val boundingBox = org.osmdroid.util.BoundingBox.fromGeoPoints(coordinates)
            mapView.zoomToBoundingBox(boundingBox, true, 100)
        }

        // ================================
        //    ORIGEN
        // ================================
        origen?.let {
            val overlayOrigen = PointsOverlay(
                points = listOf(it),
                radiusPx = radiusPx * 1.2f,
                fillColor = colorOrigen,
                strokeColor = Color.WHITE,
                strokeWidthPx = 4f
            )
            mapView.overlays.add(overlayOrigen)
        }

        // ================================
        //    DESTINO
        // ================================
        destino?.let {
            val overlayDestino = PointsOverlay(
                points = listOf(it),
                radiusPx = radiusPx * 1.2f,
                fillColor = colorDestino,
                strokeColor = Color.WHITE,
                strokeWidthPx = 4f
            )
            mapView.overlays.add(overlayDestino)
        }

        mapView.invalidate()
    }
}

