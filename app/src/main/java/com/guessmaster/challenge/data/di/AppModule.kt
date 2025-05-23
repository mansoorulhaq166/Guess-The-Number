package com.guessmaster.challenge.data.di

import android.content.Context
import android.content.res.Resources
import com.guessmaster.challenge.data.repository.GameRepository
import com.guessmaster.challenge.data.repository.SettingsRepository
import com.guessmaster.challenge.utils.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository = GameRepository()

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStoreManager: DataStoreManager): SettingsRepository =
        SettingsRepository(dataStoreManager)
}