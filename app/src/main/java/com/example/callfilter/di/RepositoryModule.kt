package com.example.callfilter.di

import android.content.Context
import android.content.SharedPreferences
import com.example.callfilter.adapter.output.AndroidContactLookupAdapter
import com.example.callfilter.adapter.output.RoomCallLogRepositoryImpl
import com.example.callfilter.adapter.output.RoomSettingsRepositoryImpl
import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.adapter.output.db.dao.CallLogDao
import com.example.callfilter.domain.port.output.CallLogRepository
import com.example.callfilter.domain.port.output.CallSettingsRepository
import com.example.callfilter.domain.port.output.ContactLookupPort
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCallSettingsRepository(
        prefs: SharedPreferences,
        blockedContactDao: BlockedContactDao
    ): CallSettingsRepository {
        return RoomSettingsRepositoryImpl(prefs, blockedContactDao)
    }

    @Provides
    @Singleton
    fun provideContactLookupPort(
        @ApplicationContext context: Context
    ): ContactLookupPort {
        return AndroidContactLookupAdapter(context)
    }

    @Provides
    @Singleton
    fun provideCallLogRepository(
        callLogDao: CallLogDao
    ): CallLogRepository {
        return RoomCallLogRepositoryImpl(callLogDao)
    }
}