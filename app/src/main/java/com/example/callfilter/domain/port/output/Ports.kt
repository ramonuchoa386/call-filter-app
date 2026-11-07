package com.example.callfilter.domain.port.output

import com.example.callfilter.domain.model.BlockedContact
import com.example.callfilter.domain.model.CallLog
import com.example.callfilter.domain.model.CallSettings
import kotlinx.coroutines.flow.Flow

interface CallSettingsRepository {
    fun getSettings(): CallSettings
    fun saveSettings(settings: CallSettings)
}

interface ContactLookupPort {
    fun isContact(phoneNumber: String): Boolean
}

interface CallLogRepository {
    suspend fun logCall(phoneNumber: String, status: String)
    fun getCallLogs(): Flow<List<CallLog>>
}

interface BlockedContactRepository {
    fun getBlockedContacts(query: String): Flow<List<BlockedContact>>
    suspend fun deleteContact(phoneNumber: String)
}