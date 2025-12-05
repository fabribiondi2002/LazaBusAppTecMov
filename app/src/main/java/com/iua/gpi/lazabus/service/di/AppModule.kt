package com.iua.gpi.lazabus.service.di

import android.content.Context
import androidx.room.Room
import com.iua.gpi.lazabus.data.AppPreferences
import com.iua.gpi.lazabus.data.local.AppDatabase
import com.iua.gpi.lazabus.data.local.dao.ViajeDao
import com.iua.gpi.lazabus.service.GeocodeService
import com.iua.gpi.lazabus.service.LocationService
import com.iua.gpi.lazabus.service.SttService
import com.iua.gpi.lazabus.service.TtsService
import com.iua.gpi.lazabus.service.interf.GeocodeServiceI
import com.iua.gpi.lazabus.service.interf.LocationServiceI
import com.iua.gpi.lazabus.service.interf.SttServiceI
import com.iua.gpi.lazabus.service.interf.TtsServiceI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Archivo que contiene los modulos de Hilt para inyectar dependencias.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideTtsService(@ApplicationContext context: Context, prefs: AppPreferences): TtsServiceI {
        return TtsService(context, prefs)
    }

    @Provides
    fun provideSttService(@ApplicationContext context: Context): SttServiceI {
        return SttService(context)
    }

    @Provides
    @Singleton
    fun provideGeocodeService(@ApplicationContext context: Context): GeocodeServiceI {
        return GeocodeService(context)
    }

    @Provides
    @Singleton
    fun provideLocationService(@ApplicationContext context: Context): LocationServiceI {
        return LocationService(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "lazabus_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideViajeDao(db: AppDatabase): ViajeDao = db.viajeDao()

}