package com.example.callfilter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.callfilter.adapter.output.db.AppDatabase
import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.adapter.output.db.dao.CallLogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "call_filter_db"
        ).build()
    }

    @Provides
    fun provideBlockedContactDao(database: AppDatabase): BlockedContactDao {
        return database.blockedContactDao()
    }

    @Provides
    fun provideCallLogDao(database: AppDatabase): CallLogDao {
        return database.callLogDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("call_filter_prefs", Context.MODE_PRIVATE)
    }
}