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

    // Para evitar crear objetos en cada draw
    private val tmpPoint = Point()

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {
        // Dibujamos sólo en la capa visible (shadow == false)
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
    radiusPx: Float = 20f,
    fillColor: Int = Color.RED,
    strokeColor: Int = Color.WHITE,
    strokeWidthPx: Float = 2f
) {
    LaunchedEffect(mapView, coordinates) {
        if (mapView == null) return@LaunchedEffect

        // breve espera para asegurar que MapView esté montado
        delay(200)

        // Eliminar overlays anteriores del tipo PointsOverlay
        val iterator = mapView.overlays.iterator()
        while (iterator.hasNext()) {
            val ov = iterator.next()
            if (ov is PointsOverlay) {
                iterator.remove()
            }
        }

        // Si no hay coordenadas, solo invalidamos y salimos
        if (coordinates.isEmpty()) {
            mapView.invalidate()
            return@LaunchedEffect
        }

        // Agregar nuestro overlay personalizado con los puntos
        val pointsOverlay = PointsOverlay(
            points = coordinates,
            radiusPx = radiusPx,
            fillColor = fillColor,
            strokeColor = strokeColor,
            strokeWidthPx = strokeWidthPx
        )
        mapView.overlays.add(pointsOverlay)

        // *** LÓGICA AGREGADA PARA CENTRAR EL MAPA EN LA RUTA ***

        // Crear un BoundingBox a partir de los puntos
        val boundingBox = org.osmdroid.util.BoundingBox.fromGeoPoints(coordinates)

        // Centrar el mapa y ajustar el zoom a la caja delimitadora
        // El 'padding' (por ejemplo, 100) es el espacio en píxeles que quieres dejar
        // como margen alrededor de la ruta para que no toque los bordes de la pantalla.
        mapView.zoomToBoundingBox(boundingBox, true, 100)

        // Forzar redibujado
        mapView.invalidate()
    }
}
