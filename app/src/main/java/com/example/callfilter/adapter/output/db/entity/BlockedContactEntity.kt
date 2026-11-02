package com.example.callfilter.adapter.output.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocked_contacts")
data class BlockedContactEntity(
    @PrimaryKey
    val phoneNumber: String,
    val name: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)