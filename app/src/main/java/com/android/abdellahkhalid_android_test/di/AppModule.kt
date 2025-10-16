package com.android.abdellahkhalid_android_test.di

import androidx.room.Room
import com.android.abdellahkhalid_android_test.data.local.PlotDatabase
import com.android.abdellahkhalid_android_test.data.repository.PlotRepositoryImpl
import com.android.abdellahkhalid_android_test.domain.repository.PlotRepository
import com.android.abdellahkhalid_android_test.presentation.viewmodel.MapViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    single {
        Room.databaseBuilder(
            androidContext(),
            PlotDatabase::class.java,
            "plot_database"
        ).build()
    }



    single { get<PlotDatabase>().plotDao() }
    
    single<PlotRepository> { PlotRepositoryImpl(get()) }
    
    viewModel { MapViewModel(get()) }
}
