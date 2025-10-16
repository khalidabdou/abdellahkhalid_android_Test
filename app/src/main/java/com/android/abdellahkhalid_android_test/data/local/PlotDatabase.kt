package com.android.abdellahkhalid_android_test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.abdellahkhalid_android_test.data.local.entity.PlotEntity

@Database(
    entities = [PlotEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlotDatabase : RoomDatabase() {
    abstract fun plotDao(): PlotDao
}
