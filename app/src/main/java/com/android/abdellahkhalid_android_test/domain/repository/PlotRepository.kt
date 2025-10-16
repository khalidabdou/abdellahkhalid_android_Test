package com.android.abdellahkhalid_android_test.domain.repository

import com.android.abdellahkhalid_android_test.domain.model.Plot
import kotlinx.coroutines.flow.Flow

interface PlotRepository {
    
    suspend fun insertPlot(plot: Plot): Long
    
    suspend fun getAllPlots(): List<Plot>
    
    suspend fun getPlotById(id: Int): Plot?
    
    suspend fun deletePlot(plot: Plot)
    
    fun observePlots(): Flow<List<Plot>>
}
