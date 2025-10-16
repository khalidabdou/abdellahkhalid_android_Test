package com.android.abdellahkhalid_android_test.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.android.abdellahkhalid_android_test.R
import com.android.abdellahkhalid_android_test.domain.model.Plot
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun GoogleMapView(
    polygonPoints: List<LatLng>,
    selectedPlot: Plot?,
    onMapClick: (LatLng) -> Unit,
    isDrawing: Boolean = false
) {
    val defaultLocation = LatLng(33.5731, -7.5898)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }
    
    LaunchedEffect(selectedPlot) {
        selectedPlot?.let { plot ->
            if (plot.coordinates.isNotEmpty()) {
                val boundsBuilder = LatLngBounds.builder()
                plot.coordinates.forEach { boundsBuilder.include(it) }
                val bounds = boundsBuilder.build()
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                cameraPositionState.animate(cameraUpdate, 1000)
            }
        }
    }
    
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = onMapClick
    ) {
        if (isDrawing) {
            polygonPoints.forEachIndexed { index, latLng ->
                Marker(
                    state = rememberMarkerState(position = latLng),
                    title = stringResource(R.string.marker_point, index + 1)
                )
            }
        }
        
        if (polygonPoints.size >= 3) {
            Polygon(
                points = polygonPoints,
                strokeColor = colorResource(R.color.polygon_blue),
                fillColor = colorResource(R.color.polygon_blue).copy(alpha = 0.3f),
                strokeWidth = 5f
            )
        }
    }
}
