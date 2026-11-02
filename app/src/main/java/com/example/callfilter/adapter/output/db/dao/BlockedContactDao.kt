package com.example.callfilter.adapter.output.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.callfilter.adapter.output.db.entity.BlockedContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedContact(contact: BlockedContactEntity)

    @Query("SELECT * FROM blocked_contacts ORDER BY addedAt DESC")
    fun getAllBlockedContacts(): Flow<List<BlockedContactEntity>>

    @Query("DELETE FROM blocked_contacts WHERE phoneNumber = :phoneNumber")
    suspend fun deleteBlockedContact(phoneNumber: String)

    @Query("SELECT EXISTS(SELECT 1 FROM blocked_contacts WHERE phoneNumber = :phoneNumber)")
    suspend fun isBlocked(phoneNumber: String): Boolean
}