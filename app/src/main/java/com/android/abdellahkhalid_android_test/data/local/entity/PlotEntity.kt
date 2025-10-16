package com.android.abdellahkhalid_android_test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plots")
data class PlotEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val coordinates: String,
    val centerLat: Double,
    val centerLng: Double,
    val createdAt: Long = System.currentTimeMillis()
)
