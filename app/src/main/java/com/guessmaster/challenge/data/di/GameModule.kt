package com.guessmaster.challenge.data.di

import com.guessmaster.challenge.domain.GameLogic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object GameModule {

    @Provides
    @ViewModelScoped
    fun provideGameLogic(): GameLogic {
        return GameLogic()
    }
}