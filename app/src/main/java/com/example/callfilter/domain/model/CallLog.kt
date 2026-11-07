package com.example.callfilter.domain.model

data class CallLog(
    val id: Long,
    val phoneNumber: String,
    val timestamp: Long,
    val status: String
)