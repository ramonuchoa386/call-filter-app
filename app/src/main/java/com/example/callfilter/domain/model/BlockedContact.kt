package com.example.callfilter.domain.model

data class BlockedContact(
    val phoneNumber: String,
    val name: String?,
    val addedAt: Long
)