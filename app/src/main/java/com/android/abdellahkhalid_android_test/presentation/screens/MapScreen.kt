package com.android.abdellahkhalid_android_test.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.android.abdellahkhalid_android_test.R
import com.android.abdellahkhalid_android_test.domain.model.Plot
import com.android.abdellahkhalid_android_test.presentation.components.DeleteConfirmationBottomSheet
import com.android.abdellahkhalid_android_test.presentation.components.GoogleMapView
import com.android.abdellahkhalid_android_test.presentation.components.PlotDropdown
import com.android.abdellahkhalid_android_test.presentation.components.SavePlotDialog
import com.android.abdellahkhalid_android_test.presentation.viewmodel.MapViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(
    viewModel: MapViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val polygonPoints by viewModel.polygonPoints.collectAsState()
    val isDrawing by viewModel.isDrawing.collectAsState()
    val showSaveDialog by viewModel.showSaveDialog.collectAsState()
    val plots by viewModel.plots.collectAsState()
    val selectedPlot by viewModel.selectedPlot.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    
    var plotToDelete by remember { mutableStateOf<Plot?>(null) }
    
    androidx.compose.runtime.LaunchedEffect(saveSuccess) {
        saveSuccess?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMapView(
            polygonPoints = polygonPoints,
            selectedPlot = selectedPlot,
            onMapClick = { latLng ->
                viewModel.addPoint(latLng)
            },
            isDrawing = isDrawing
        )
        
        if (plots.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.dark_background)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                PlotDropdown(
                    plots = plots,
                    selectedPlot = selectedPlot,
                    onPlotSelected = { plot ->
                        viewModel.selectPlot(plot)
                    },
                    onPlotDeleted = { plot ->
                        plotToDelete = plot
                    },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 60.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.dark_background)
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        if (isDrawing) {
                            viewModel.stopDrawing()
                        } else {
                            viewModel.startDrawing()
                            Toast.makeText(
                                context,
                                context.getString(R.string.toast_tap_map),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    containerColor = if (isDrawing) colorResource(R.color.ios_green) else colorResource(R.color.ios_blue),
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (isDrawing) Icons.Default.Done else Icons.Default.Create,
                            contentDescription = stringResource(if (isDrawing) R.string.content_desc_stop else R.string.content_desc_draw),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(if (isDrawing) R.string.button_stop else R.string.button_draw),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                if (polygonPoints.isNotEmpty() && !isDrawing && selectedPlot == null) {
                    FloatingActionButton(
                        onClick = {
                            if (polygonPoints.size < 3) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.toast_need_3_points),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.showSaveDialog()
                            }
                        },
                        containerColor = colorResource(R.color.ios_orange),
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = stringResource(R.string.content_desc_save),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.button_save),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
        
        if (showSaveDialog) {
            SavePlotDialog(
                onDismiss = { viewModel.hideSaveDialog() },
                onSave = { name ->
                    viewModel.savePlot(name)
                }
            )
        }
        
        plotToDelete?.let { plot ->
            DeleteConfirmationBottomSheet(
                plotName = plot.name,
                onDismiss = { plotToDelete = null },
                onConfirmDelete = {
                    viewModel.deletePlot(plot)
                    Toast.makeText(
                        context,
                        context.getString(R.string.toast_plot_deleted, plot.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}
