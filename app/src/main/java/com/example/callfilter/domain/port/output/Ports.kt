package com.example.callfilter.domain.port.output

import com.example.callfilter.domain.model.CallSettings

interface CallSettingsRepository {
    fun getSettings(): CallSettings
    fun saveSettings(settings: CallSettings)
}

interface ContactLookupPort {
    fun isContact(phoneNumber: String): Boolean
}