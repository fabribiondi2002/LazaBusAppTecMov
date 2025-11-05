package com.iua.gpi.lazabus.service.di

import android.content.Context
import com.iua.gpi.lazabus.service.SttService
import com.iua.gpi.lazabus.service.TtsService
import com.iua.gpi.lazabus.service.interf.SttServiceI
import com.iua.gpi.lazabus.service.interf.TtsServiceI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provee el Context de la Aplicación
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    // 💡 Aquí Hilt sabe que cuando se pida TtsService, debe crear un AndroidTtsService
    // con el ApplicationContext.
    @Provides
    @Singleton
    fun provideTtsService(@ApplicationContext context: Context): TtsServiceI {
        // Usar AndroidTtsService y hacer que viva mientras viva la app (Singleton)
        return TtsService(context)
    }

    @Provides
    fun provideSttService(@ApplicationContext context: Context): SttServiceI {
        return SttService(context)
    }
}