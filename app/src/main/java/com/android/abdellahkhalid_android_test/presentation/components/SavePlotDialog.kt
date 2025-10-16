package com.android.abdellahkhalid_android_test.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.abdellahkhalid_android_test.R

@Composable
fun SavePlotDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var plotName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_save_plot_title)) },
        text = {
            Column {
                Text(stringResource(R.string.dialog_save_plot_message))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = plotName,
                    onValueChange = { plotName = it },
                    label = { Text(stringResource(R.string.label_plot_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (plotName.isNotBlank()) {
                        onSave(plotName.trim())
                    }
                },
                enabled = plotName.isNotBlank()
            ) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        }
    )
}
