package com.android.abdellahkhalid_android_test.domain.model

import com.google.android.gms.maps.model.LatLng

data class Plot(
    val id: Int = 0,
    val name: String,
    val coordinates: List<LatLng>,
    val centerLat: Double,
    val centerLng: Double,
    val createdAt: Long = System.currentTimeMillis()
)
