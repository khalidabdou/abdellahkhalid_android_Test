package com.android.abdellahkhalid_android_test.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.abdellahkhalid_android_test.presentation.screens.MapScreen
import com.android.abdellahkhalid_android_test.presentation.ui.theme.Abdellahkhalid_android_TestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Abdellahkhalid_android_TestTheme {
                MapScreen()
            }
        }
    }
}