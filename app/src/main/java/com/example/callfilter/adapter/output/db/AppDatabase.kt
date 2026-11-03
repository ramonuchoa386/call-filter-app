package com.example.callfilter.adapter.output.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.callfilter.adapter.output.db.dao.BlockedContactDao
import com.example.callfilter.adapter.output.db.dao.CallLogDao
import com.example.callfilter.adapter.output.db.entity.BlockedContactEntity
import com.example.callfilter.adapter.output.db.entity.CallLogEntity

@Database(
    entities = [BlockedContactEntity::class, CallLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun blockedContactDao(): BlockedContactDao
    abstract fun callLogDao(): CallLogDao
}