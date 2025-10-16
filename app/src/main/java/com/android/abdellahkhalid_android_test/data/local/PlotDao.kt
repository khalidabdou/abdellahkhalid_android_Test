package com.android.abdellahkhalid_android_test.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.abdellahkhalid_android_test.data.local.entity.PlotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlotDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plot: PlotEntity): Long
    
    @Query("SELECT * FROM plots ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<PlotEntity>>
    
    @Query("SELECT * FROM plots ORDER BY createdAt DESC")
    suspend fun getAll(): List<PlotEntity>
    
    @Query("SELECT * FROM plots WHERE id = :id")
    suspend fun getById(id: Int): PlotEntity?
    
    @Delete
    suspend fun delete(plot: PlotEntity)
    
    @Query("DELETE FROM plots")
    suspend fun deleteAll()
}
