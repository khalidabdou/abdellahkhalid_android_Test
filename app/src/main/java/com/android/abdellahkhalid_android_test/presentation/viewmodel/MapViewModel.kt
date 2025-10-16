package com.android.abdellahkhalid_android_test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.abdellahkhalid_android_test.domain.model.Plot
import com.android.abdellahkhalid_android_test.domain.repository.PlotRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: PlotRepository
) : ViewModel() {
    
    private val _plots = MutableStateFlow<List<Plot>>(emptyList())
    val plots: StateFlow<List<Plot>> = _plots.asStateFlow()
    
    private val _selectedPlot = MutableStateFlow<Plot?>(null)
    val selectedPlot: StateFlow<Plot?> = _selectedPlot.asStateFlow()
    
    private val _polygonPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val polygonPoints: StateFlow<List<LatLng>> = _polygonPoints.asStateFlow()
    
    private val _isDrawing = MutableStateFlow(false)
    val isDrawing: StateFlow<Boolean> = _isDrawing.asStateFlow()
    
    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog.asStateFlow()
    
    private val _saveSuccess = MutableStateFlow<String?>(null)
    val saveSuccess: StateFlow<String?> = _saveSuccess.asStateFlow()
    
    init {
        loadPlots()
    }
    
    private fun loadPlots() {
        viewModelScope.launch {
            repository.observePlots().collect { plotList ->
                _plots.value = plotList
            }
        }
    }
    
    fun addPoint(latLng: LatLng) {
        if (_isDrawing.value) {
            _polygonPoints.value = _polygonPoints.value + latLng
        }
    }
    
    fun startDrawing() {
        _isDrawing.value = true
        _polygonPoints.value = emptyList()
        _selectedPlot.value = null
    }
    
    fun stopDrawing() {
        _isDrawing.value = false
    }
    
    fun clearPolygon() {
        _polygonPoints.value = emptyList()
        _selectedPlot.value = null
    }
    
    fun showSaveDialog() {
        if (_polygonPoints.value.size >= 3) {
            _showSaveDialog.value = true
        }
    }
    
    fun hideSaveDialog() {
        _showSaveDialog.value = false
    }
    
    fun savePlot(name: String) {
        viewModelScope.launch {
            if (_polygonPoints.value.size >= 3) {
                val centerLat = _polygonPoints.value.map { it.latitude }.average()
                val centerLng = _polygonPoints.value.map { it.longitude }.average()
                
                val plot = Plot(
                    name = name,
                    coordinates = _polygonPoints.value,
                    centerLat = centerLat,
                    centerLng = centerLng
                )
                
                repository.insertPlot(plot)
                clearPolygon()
                _showSaveDialog.value = false
                _isDrawing.value = false
                _saveSuccess.value = "Plot '$name' saved successfully!"
            }
        }
    }
    
    fun clearSuccessMessage() {
        _saveSuccess.value = null
    }
    
    fun selectPlot(plot: Plot) {
        _selectedPlot.value = plot
        _polygonPoints.value = plot.coordinates
        _isDrawing.value = false
    }
    
    fun deletePlot(plot: Plot) {
        viewModelScope.launch {
            repository.deletePlot(plot)
            if (_selectedPlot.value?.id == plot.id) {
                clearPolygon()
            }
        }
    }
}