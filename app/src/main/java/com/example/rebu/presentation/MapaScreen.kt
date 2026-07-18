package com.example.rebu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.Text
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapaScreen(viewModel: PaqueteActivoViewModel = viewModel()) {
    LaunchedEffect(Unit) { viewModel.iniciarPolling() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        // Estas dos líneas son la clave para centrar el contenido en pantallas redondas
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = if (viewModel.hayPedido) viewModel.cliente else "Esperando pedido...",
            modifier = Modifier.padding(bottom = 8.dp) // Da un pequeño margen si aparece el mapa
        )

        if (viewModel.hayPedido) {
            AndroidView(
                factory = { context ->
                    Configuration.getInstance().userAgentValue = context.packageName
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        controller.setZoom(15.0)

                        // Mejoras para la pantalla táctil del smartwatch
                        setMultiTouchControls(false)
                        isClickable = false
                    }
                },
                update = { mapView ->
                    val punto = GeoPoint(viewModel.lat, viewModel.lng)
                    mapView.controller.setCenter(punto)
                    mapView.overlays.clear()
                    mapView.overlays.add(
                        Marker(mapView).apply {
                            position = punto
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            title = viewModel.direccion
                        }
                    )
                    mapView.invalidate()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}