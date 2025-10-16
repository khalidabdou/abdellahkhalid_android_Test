package com.android.abdellahkhalid_android_test.data.repository

import com.android.abdellahkhalid_android_test.data.local.PlotDao
import com.android.abdellahkhalid_android_test.data.local.entity.PlotEntity
import com.android.abdellahkhalid_android_test.domain.model.Plot
import com.android.abdellahkhalid_android_test.domain.repository.PlotRepository
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlotRepositoryImpl(
    private val plotDao: PlotDao
) : PlotRepository {
    
    private val gson = Gson()
    
    override suspend fun insertPlot(plot: Plot): Long {
        val entity = plot.toEntity()
        return plotDao.insert(entity)
    }
    
    override suspend fun getAllPlots(): List<Plot> {
        return plotDao.getAll().map { it.toDomain() }
    }
    
    override suspend fun getPlotById(id: Int): Plot? {
        return plotDao.getById(id)?.toDomain()
    }
    
    override suspend fun deletePlot(plot: Plot) {
        val entity = plot.toEntity()
        plotDao.delete(entity)
    }
    
    override fun observePlots(): Flow<List<Plot>> {
        return plotDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    private fun Plot.toEntity(): PlotEntity {
        val coordinatesJson = gson.toJson(coordinates.map { mapOf("lat" to it.latitude, "lng" to it.longitude) })
        return PlotEntity(
            id = id,
            name = name,
            coordinates = coordinatesJson,
            centerLat = centerLat,
            centerLng = centerLng,
            createdAt = createdAt
        )
    }
    
    private fun PlotEntity.toDomain(): Plot {
        val type = object : TypeToken<List<Map<String, Double>>>() {}.type
        val coordsList: List<Map<String, Double>> = gson.fromJson(coordinates, type)
        val latLngList = coordsList.map { LatLng(it["lat"] ?: 0.0, it["lng"] ?: 0.0) }
        
        return Plot(
            id = id,
            name = name,
            coordinates = latLngList,
            centerLat = centerLat,
            centerLng = centerLng,
            createdAt = createdAt
        )
    }
}
